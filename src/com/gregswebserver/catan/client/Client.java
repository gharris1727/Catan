package com.gregswebserver.catan.client;

import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.input.InputListener;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.renderer.RenderEvent;
import com.gregswebserver.catan.client.renderer.RenderThread;
import com.gregswebserver.catan.client.structure.ConnectionInfo;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.structure.ServerLogin;
import com.gregswebserver.catan.client.structure.ServerPool;
import com.gregswebserver.catan.client.ui.PopupWindow;
import com.gregswebserver.catan.client.ui.RenderManager;
import com.gregswebserver.catan.client.ui.ingame.TeamColors;
import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.chat.ChatEvent;
import com.gregswebserver.catan.common.chat.ChatThread;
import com.gregswebserver.catan.common.config.ConfigurationException;
import com.gregswebserver.catan.common.config.PropertiesFile;
import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.InternalEvent;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.event.GameControlEvent;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameEventType;
import com.gregswebserver.catan.common.game.gameplay.trade.Trade;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.network.ClientConnection;
import com.gregswebserver.catan.common.network.NetEvent;
import com.gregswebserver.catan.common.network.NetEventType;
import com.gregswebserver.catan.common.resources.PropertiesFileInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.structure.event.ControlEvent;
import com.gregswebserver.catan.common.structure.event.LobbyEvent;
import com.gregswebserver.catan.common.structure.event.LobbyEventType;
import com.gregswebserver.catan.common.structure.game.GameProgress;
import com.gregswebserver.catan.common.structure.lobby.Lobby;
import com.gregswebserver.catan.common.structure.lobby.LobbyConfig;
import com.gregswebserver.catan.common.structure.lobby.MatchmakingPool;

import java.net.UnknownHostException;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 * Everything is handled by a main event queue, which them distributes the events to where they need to go.
 * Client events are intercepted and acted upon.
 */
public class Client extends CoreThread {

    private static final PropertiesFileInfo configFile;
    private static final PropertiesFileInfo serverFile;
    private static final PropertiesFileInfo uiLayoutFile;

    static {
        configFile = new PropertiesFileInfo("client.properties", "Client configuration file");
        serverFile = new PropertiesFileInfo("client/servers.properties", "Login details for servers");
        uiLayoutFile = new PropertiesFileInfo("graphics/graphics.properties", "Graphics configuration");
    }

    private PropertiesFile config;
    private PropertiesFile teamColors;

    private ChatThread chatThread;
    private GameManager gameManager;
    private RenderThread renderThread;

    private RenderManager manager;
    private ClientConnection connection;

    private ServerPool serverPool;
    private Username username;
    private MatchmakingPool matchmakingPool;

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
            case Display_Popup:
                manager.displayPopup((PopupWindow) event.getPayload());
                break;
            case Server_Change:
                serverPool.remove((ConnectionInfo) event.getOrigin());
                serverPool.add((ConnectionInfo) event.getPayload());
                manager.serverConnectMenu.update();
                break;
            case Register_Account:
                try {
                    ServerLogin server = ((ConnectionInfo) event.getPayload()).createServerLogin();
                    username = server.login.username;
                    connection = new ClientConnection(this, server.remote, NetEventType.Register, server.login);
                    connection.connect();
                } catch (UnknownHostException | NumberFormatException e) {
                    logger.log(e, LogLevel.WARN);
                    manager.displayServerDisconnectingScreen(e.getMessage());
                }
                break;
            case Net_Connect:
                manager.displayServerConnectingScreen();
                try {
                    ServerLogin server = ((ConnectionInfo) event.getPayload()).createServerLogin();
                    username = server.login.username;
                    connection = new ClientConnection(this, server.remote, NetEventType.Log_In, server.login);
                    connection.connect();
                } catch (UnknownHostException | NumberFormatException e) {
                    logger.log(e, LogLevel.WARN);
                    manager.displayServerDisconnectingScreen(e.getMessage());
                }
                break;
            case Net_Disconnect:
                disconnect("Quitting");
                break;
            case Net_Clear:
                manager.displayServerConnectMenu(serverPool);
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
            case Space_Clicked:
                manager.gameScreen.spaceClicked((Coordinate) event.getPayload());
                break;
            case Tile_Rob:
                gameManager.local(new GameEvent(username, GameEventType.Player_Move_Robber, event.getPayload()));
                break;
            case End_Turn:
                gameManager.local(new GameEvent(username, GameEventType.Turn_Advance, null));
                break;
            case Edge_Clicked:
                manager.gameScreen.edgeClicked((Coordinate) event.getPayload());
                break;
            case Road_Purchase:
                gameManager.local(new GameEvent(username, GameEventType.Build_Road, event.getPayload()));
                break;
            case Vertex_Clicked:
                manager.gameScreen.vertexClicked((Coordinate) event.getPayload());
                break;
            case Settlement_Purchase:
                gameManager.local(new GameEvent(username, GameEventType.Build_Settlement, event.getPayload()));
                break;
            case City_Purchase:
                gameManager.local(new GameEvent(username, GameEventType.Build_City, event.getPayload()));
                break;
            case Trade_Clicked:
                manager.gameScreen.tradeClicked((Trade) event.getPayload());
                break;
            case Make_Trade:
                gameManager.local(new GameEvent(username, GameEventType.Make_Trade, event.getPayload()));
                break;
            case History_Clicked:
                manager.gameScreen.timelineClicked((Integer) event.getPayload());
                break;
            case History_Jump:
                gameManager.jumpToEvent((Integer) event.getPayload());
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public void localSuccess(GameControlEvent event) {
        sendEvent((ExternalEvent) event.getPayload());
    }

    public void localFailure(EventConsumerException e) {
        logger.log(this + " Local failure", e, LogLevel.WARN);
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
                manager.displayLobbyJoinMenu(matchmakingPool);
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
                        manager.displayInLobbyScreen(matchmakingPool.getLobbyList().userGetLobby(username));
                    break;
                case Lobby_Leave:
                    if (username.equals(event.getOrigin()))
                        manager.displayLobbyJoinMenu(matchmakingPool);
                    break;
                case Game_Start:
                    break;
                case Game_Join:
                    break;
                case Game_Leave:
                    break;
                case Game_Sync:
                    gameManager = new GameManager(this, (GameProgress) event.getPayload());
                    manager.displayGameScreen(username, gameManager, new TeamColors(teamColors));
                    break;
                default:
                    throw new IllegalStateException();
            }
            //Force update any relevant screens
            if (manager.lobbyJoinMenu != null)
                manager.lobbyJoinMenu.update();
            if (manager.lobbyScreen != null)
                manager.lobbyScreen.update();
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
                manager.displayServerDisconnectingScreen((String) event.getPayload());
                break;
            case External_Event:
                externalEvent((ExternalEvent) event.getPayload());
                break;
            default:
                throw new IllegalStateException();
        }
    }

    public void gameUpdate() {
        manager.gameScreen.update();
    }

    private void startup() {
        config = ResourceLoader.getPropertiesFile(configFile);
        PropertiesFileInfo localeFile = new PropertiesFileInfo("locale/" + config.get("locale") + ".properties", "Locale information");
        PropertiesFile locale = ResourceLoader.getPropertiesFile(localeFile);
        PropertiesFileInfo teamColorsFile = new PropertiesFileInfo("graphics/teams.properties","Team colors configuration");
        teamColors = ResourceLoader.getPropertiesFile(teamColorsFile);
        PropertiesFile layout = ResourceLoader.getPropertiesFile(uiLayoutFile);
        manager = new RenderManager();
        PropertiesFileInfo styleFile = new PropertiesFileInfo("ui/"+config.get("style") + ".properties", "Style Information");
        PropertiesFile style = ResourceLoader.getPropertiesFile(styleFile);
        manager.setConfig(new UIConfig(style, layout, locale));
        new ClientWindow(this, new InputListener(this, manager));
        serverPool = new ServerPool(serverFile);
        manager.displayServerConnectMenu(serverPool);
        renderThread = new RenderThread(logger, manager);
        renderThread.start();
    }

    private void shutdown() {
        //Shut down the client and all running threads.
        disconnect("Quitting");
        if (renderThread != null && renderThread.isRunning())
            renderThread.stop();
//        if (chatThread != null && chatThread.isRunning())
//            chatThread.stop();
        if (gameManager != null)
            gameManager.stop();
        serverPool.save();
        try {
            config.save();
        } catch (ConfigurationException e) {
            logger.log("Unable to save configuration", e, LogLevel.WARN);
        }
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
