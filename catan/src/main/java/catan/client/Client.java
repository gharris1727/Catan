package catan.client;

import catan.client.graphics.ui.UIConfig;
import catan.client.input.InputListener;
import catan.client.input.UserEvent;
import catan.client.renderer.BaseRegion;
import catan.client.renderer.RenderEvent;
import catan.client.renderer.RenderEventType;
import catan.client.renderer.RenderThread;
import catan.client.structure.ConnectionInfo;
import catan.client.structure.GameManager;
import catan.client.structure.ServerLogin;
import catan.client.structure.ServerPool;
import catan.client.ui.ClientScreen;
import catan.client.ui.connecting.ConnectingScreen;
import catan.client.ui.disconnecting.DisconnectingScreen;
import catan.client.ui.game.playing.PlayingScreenRegion;
import catan.client.ui.game.postgame.PostGameScreenRegion;
import catan.client.ui.game.spectate.SpectateScreenRegion;
import catan.client.ui.lobby.LobbyScreen;
import catan.client.ui.lobbyjoinmenu.LobbyJoinMenu;
import catan.client.ui.serverconnectmenu.ServerConnectMenu;
import catan.client.ui.taskbar.FileMenu;
import catan.client.ui.taskbar.TaskbarMenu;
import catan.common.CoreThread;
import catan.common.IllegalStateException;
import catan.common.chat.ChatEvent;
import catan.common.chat.ChatThread;
import catan.common.config.ConfigurationException;
import catan.common.config.PropertiesFile;
import catan.common.crypto.AuthToken;
import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.ExternalEvent;
import catan.common.event.InternalEvent;
import catan.common.game.event.GameControlEvent;
import catan.common.game.event.GameEvent;
import catan.common.log.LogLevel;
import catan.common.log.Logger;
import catan.common.network.ClientConnection;
import catan.common.network.NetEvent;
import catan.common.network.NetEventType;
import catan.common.resources.PropertiesFileInfo;
import catan.common.resources.ResourceLoader;
import catan.common.structure.event.ControlEvent;
import catan.common.structure.event.LobbyEvent;
import catan.common.structure.event.LobbyEventType;
import catan.common.structure.game.GameProgress;
import catan.common.structure.lobby.Lobby;
import catan.common.structure.lobby.LobbyConfig;
import catan.common.structure.lobby.MatchmakingPool;

import java.net.UnknownHostException;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends CoreThread implements GameManagerListener {

    private static final PropertiesFileInfo configFile;
    private static final PropertiesFileInfo serverFile;
    private static final PropertiesFileInfo uiLayoutFile;

    static {
        configFile = new PropertiesFileInfo("client.properties", "Client configuration file");
        serverFile = new PropertiesFileInfo("client/servers.properties", "Login details for servers");
        uiLayoutFile = new PropertiesFileInfo("client/layout.properties", "UI Layout information");
    }

    private PropertiesFile config;

    private ChatThread chatThread;
    private GameManager gameManager;
    private RenderThread renderThread;

    private ClientConnection connection;

    private ServerPool serverPool;
    private Username username;
    private MatchmakingPool matchmakingPool;
    private InputListener listener;
    private ClientWindow window;

    public Client() {
        this(new Logger());
    }

    public Client(Logger logger) {
        super(logger);
        addEvent(new ClientEvent(this, ClientEventType.Startup, null));
        start();
    }

    @Override
    protected void externalEvent(ExternalEvent event) {
        if (event instanceof ChatEvent && chatThread != null)
            chatThread.addEvent((ChatEvent) event);
        else if (event instanceof GameEvent)
            gameEvent((GameEvent) event);
        else if (event instanceof ControlEvent)
            controlEvent((ControlEvent) event);
        else if (event instanceof LobbyEvent)
            lobbyEvent((LobbyEvent) event);
        else
            throw new IllegalStateException();
    }

    @Override
    protected void internalEvent(InternalEvent event) {
        if (event instanceof RenderEvent && renderThread != null)
            renderThread.addEvent((RenderEvent) event);
        else if (event instanceof ClientEvent)
            clientEvent((ClientEvent) event);
        else if (event instanceof UserEvent)
            userEvent((UserEvent) event);
        else
            throw new IllegalStateException();
    }

    // Client maintenance events, for startup shutdown etc.
    private void clientEvent(ClientEvent event) {
        switch (event.getType()) {
            case Startup:
                startup();
                break;
            case Quit_All:
                shutdown();
                break;
            default:
                throw new IllegalStateException();
        }
    }

    // Interpreting events from user input to go to the server.
    private void userEvent(UserEvent event) {
        ExternalEvent outgoing;
        switch (event.getType()) {
            case Shutdown:
                addEvent(new ClientEvent(this, ClientEventType.Quit_All, null));
                break;
            case Server_Remove:
                serverPool.remove((ConnectionInfo) event.getPayload());
                refreshScreen();
                break;
            case Server_Add:
                serverPool.add((ConnectionInfo) event.getPayload());
                refreshScreen();
                break;
            case Register_Account:
                try {
                    ServerLogin server = ((ConnectionInfo) event.getPayload()).createServerLogin();
                    username = server.login.username;
                    connection = new ClientConnection(this, server.remote, NetEventType.Register, server.login);
                    connection.connect();
                } catch (UnknownHostException | NumberFormatException e) {
                    logger.log(e, LogLevel.WARN);
                    changeScreen(new DisconnectingScreen(e.getMessage()));
                }
                break;
            case Net_Connect:
                changeScreen(new ConnectingScreen());
                try {
                    ServerLogin server = ((ConnectionInfo) event.getPayload()).createServerLogin();
                    username = server.login.username;
                    connection = new ClientConnection(this, server.remote, NetEventType.Log_In, server.login);
                    connection.connect();
                } catch (UnknownHostException | NumberFormatException e) {
                    logger.log(e, LogLevel.WARN);
                    changeScreen(new DisconnectingScreen(e.getMessage()));
                }
                break;
            case Net_Disconnect:
                disconnect("Quitting");
                break;
            case Net_Clear:
                changeScreen(new ServerConnectMenu(serverPool));
                break;
            case Lobby_Create:
                outgoing = new LobbyEvent(username, LobbyEventType.Lobby_Create, new LobbyConfig(username));
                sendEvent(outgoing);
                break;
            case Lobby_Join:
                outgoing = new LobbyEvent(username, LobbyEventType.Lobby_Join, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Quit:
                outgoing = new LobbyEvent(username, LobbyEventType.Lobby_Leave, null);
                sendEvent(outgoing);
                break;
            case Lobby_Edit:
                outgoing = new LobbyEvent(username, LobbyEventType.Lobby_Change_Config, event.getPayload());
                sendEvent(outgoing);
                break;
            case Lobby_Start:
                Lobby lobby = matchmakingPool.getLobbyList().userGetLobby(username);
                outgoing = new LobbyEvent(username, LobbyEventType.Game_Start, lobby.getGameSettings());
                sendEvent(outgoing);
                break;
            case History_Jump:
                gameManager.jumpToEvent((Integer) event.getPayload());
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void localSuccess(GameControlEvent event) {
        switch (event.getType()) {
            case Test:
                sendEvent((ExternalEvent) event.getPayload());
                break;
            case Execute:
            case Undo:
                refreshScreen();
                break;
        }
    }

    @Override
    public void localFailure(EventConsumerException e) {
        logger.log(this + " Local failure", e, LogLevel.WARN);
    }

    @Override
    public void remoteSuccess(GameControlEvent event) {
    }

    @Override
    public void remoteFailure(EventConsumerException e) {
        logger.log(this + " Remote failure", e, LogLevel.ERROR);
    }

    private void gameEvent(GameEvent event) {
        if (gameManager != null)
            gameManager.remote(event);
    }

    // Control events sent by the server to affect the local client.
    private void controlEvent(ControlEvent event) {
        switch (event.getType()) {
            case Server_Disconnect:
                throw new RuntimeException("Unimplemented");
            case Name_Change:
            case Pass_Change:
            case Delete_Account:
                throw new IllegalStateException();
            case Pass_Change_Success:
                throw new RuntimeException("Unimplemented");
            case Pass_Change_Failure:
                throw new RuntimeException("Unimplemented");
            case User_Pool_Sync:
                matchmakingPool = (MatchmakingPool) event.getPayload();
                changeScreen(new LobbyJoinMenu(matchmakingPool));
                break;
            case Client_Disconnect:
                throw new IllegalStateException();
            default:
                throw new IllegalStateException();
        }
    }

    private void lobbyEvent(LobbyEvent event) {
        try {
            //Update the matchmaking pool
            matchmakingPool.execute(event);
            //Check for anything to do locally.
            switch (event.getType()) {
                case User_Connect:
                    break;
                case User_Disconnect:
                    break;
                case Lobby_Change_Config:
                    break;
                case Lobby_Create:
                case Lobby_Join:
                    if (username.equals(event.getOrigin()))
                        changeScreen(new LobbyScreen(matchmakingPool.getLobbyList().userGetLobby(username)));
                    break;
                case Lobby_Leave:
                    if (username.equals(event.getOrigin()))
                        changeScreen(new LobbyJoinMenu(matchmakingPool));
                    break;
                case Game_Start:
                    break;
                case Game_Join:
                    break;
                case Game_Leave:
                    break;
                case Game_Sync:
                    gameManager = new GameManager(this, logger, token.username, (GameProgress) event.getPayload());
                    if (gameManager.getLocalPlayer() != null)
                        changeScreen(new PlayingScreenRegion(gameManager));
                    else
                        changeScreen(new SpectateScreenRegion(gameManager));
                    break;
                case Game_Finish:
                    if (gameManager != null && gameManager.getLocalPlayer() != null)
                        changeScreen(new PostGameScreenRegion(gameManager));
                    break;
                default:
                    throw new IllegalStateException();
            }
            //Force refresh any relevant screens
            refreshScreen();
        } catch (EventConsumerException e) {
            logger.log(e, LogLevel.ERROR);
        }

    }

    // Manage events coming from the network connection.
    @Override
    public void netEvent(NetEvent event) {
        switch (event.getType()) {
            case Register:
            case Log_In:
                throw new IllegalStateException();
            case Auth_Success:
                token = (AuthToken) event.getPayload();
                break;
            case Register_Success:
            case Register_Failure:
                //TODO: determine something special to do to confirm/deny registration.
            case Auth_Failure:
            case Disconnect:
            case Link_Error:
                changeScreen(new DisconnectingScreen((String) event.getPayload()));
                break;
            case External_Event:
                externalEvent((ExternalEvent) event.getPayload());
                break;
            default:
                throw new IllegalStateException();
        }
    }

    private void refreshScreen() {
        renderThread.addEvent(new RenderEvent(this, RenderEventType.Screen_Refresh, null));
    }

    private void changeScreen(ClientScreen screen) {
        renderThread.addEvent(new RenderEvent(this, RenderEventType.Screen_Update, screen));
    }

    private void addMenu(TaskbarMenu menu) {
        renderThread.addEvent(new RenderEvent(this, RenderEventType.Taskbar_Add, menu));
    }

    private void startup() {
        //Load base configuration details
        config = ResourceLoader.getPropertiesFile(configFile);
        PropertiesFileInfo localeFile = new PropertiesFileInfo("locale/" + config.get("locale") + ".properties", "Locale information");
        PropertiesFileInfo teamColorsFile = new PropertiesFileInfo("client/teams.properties","Team colors configuration");
        PropertiesFileInfo styleFile = new PropertiesFileInfo("ui/"+config.get("style") + ".properties", "Style Information");
        //Load the auxiliary configuration files
        PropertiesFile locale = ResourceLoader.getPropertiesFile(localeFile);
        PropertiesFile layout = ResourceLoader.getPropertiesFile(uiLayoutFile);
        PropertiesFile style = ResourceLoader.getPropertiesFile(styleFile);
        PropertiesFile teamColors = ResourceLoader.getPropertiesFile(teamColorsFile);
        //Create shared resources.
        serverPool = new ServerPool(serverFile);
        BaseRegion base = new BaseRegion();
        listener = new InputListener(this, base);
        window = new ClientWindow(this, listener);
        //Start and configure the renderer.
        renderThread = new RenderThread(logger, base);
        renderThread.addEvent(new RenderEvent(this, RenderEventType.Set_Configuration, new UIConfig(style, layout, locale, teamColors)));
        changeScreen(new ServerConnectMenu(serverPool));
        addMenu(new FileMenu());
        renderThread.start();
    }

    private void shutdown() {
        //Shut down the client and all running threads.
        disconnect("Quitting");
        if (renderThread.isRunning())
            renderThread.join();
//        if (chatThread.isRunning())
//            chatThread.join();
        if (gameManager != null)
            gameManager.join();
        if (listener.getThread().isRunning())
            listener.getThread().join();
        serverPool.save();
        try {
            config.save();
        } catch (ConfigurationException e) {
            logger.log("Unable to save configuration", e, LogLevel.WARN);
        }
        window.dispose();
        window.waitForClose();
        stop();
    }

    private void disconnect(String reason) {
        if (connection != null && connection.isOpen()) {
            connection.sendEvent(new NetEvent(token, NetEventType.Disconnect, reason));
            connection.disconnect();
        }
    }

    private void sendEvent(ExternalEvent event) {
        if (connection != null && connection.isOpen())
            connection.sendEvent(event);
    }

    public String toString() {
        return "Client(" + username + ")";
    }
}
