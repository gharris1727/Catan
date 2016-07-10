package catan.server;


import catan.common.CoreThread;
import catan.common.IllegalStateException;
import catan.common.config.ConfigurationException;
import catan.common.config.PropertiesFile;
import catan.common.crypto.AuthToken;
import catan.common.crypto.AuthenticationException;
import catan.common.crypto.Password;
import catan.common.crypto.Username;
import catan.common.event.ExternalEvent;
import catan.common.event.InternalEvent;
import catan.common.game.event.GameEvent;
import catan.common.log.LogLevel;
import catan.common.log.Logger;
import catan.common.network.NetEvent;
import catan.common.network.NetEventType;
import catan.common.network.ServerConnection;
import catan.common.resources.PropertiesFileInfo;
import catan.common.resources.ResourceLoader;
import catan.common.structure.UserInfo;
import catan.common.structure.UserLogin;
import catan.common.structure.event.ControlEvent;
import catan.common.structure.event.ControlEventType;
import catan.common.structure.event.LobbyEvent;
import catan.common.structure.event.LobbyEventType;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;
import catan.common.structure.lobby.Lobby;
import catan.common.structure.lobby.LobbyState;
import catan.common.structure.lobby.MatchmakingPool;
import catan.server.console.CommandLine;
import catan.server.console.Console;
import catan.server.console.ServerWindow;
import catan.server.structure.*;

import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.SecureRandom;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends CoreThread {

    private static final PropertiesFileInfo configFile;

    private PropertiesFile config;

    private CommandLine commandLine;
    private ServerWindow window;
    private UserDatabase database;
    private ConnectionPool connectionPool;
    private MatchmakingPool matchmakingPool;
    private GamePool gamePool;

    private ServerSocket socket;
    private boolean listening;
    private Thread listen;

    static {
        configFile = new PropertiesFileInfo("server.properties", "Server Configuration");
    }


    public Server(int port) {
        this(new Logger(), port);
    }

    public Server(Logger logger, int port) {
        super(logger);
        startup(port);
        start();
    }

    @Override
    protected void externalEvent(ExternalEvent event) {
        if (event instanceof ControlEvent)
            controlEvent((ControlEvent) event);
        else if (event instanceof LobbyEvent)
            lobbyEvent((LobbyEvent) event);
        else if (event instanceof GameEvent)
            gameEvent((GameEvent) event);
        else
            throw new IllegalStateException();
    }

    @Override
    protected void internalEvent(InternalEvent event) {
        if (event instanceof ServerEvent)
            serverEvent((ServerEvent) event);
        else
            throw new IllegalStateException();
    }

    private void serverEvent(ServerEvent event) {
        ServerConnection connection;
        switch (event.getType()) {
            case Quit_All:
                shutdown();
                break;
            case Client_Connect:
                connection = new ServerConnection(this, (Socket) event.getPayload());
                connectionPool.addConnection(connection);
                connection.connect();
                break;
            case User_Connect:
                connection = (ServerConnection) event.getOrigin();
                Username username = (Username) event.getPayload();
                try {
                    //Keep track of their connection id in the database.
                    connectionPool.addUser(username, connection);
                    //Send them a sync of the current client pool state.
                    connection.sendEvent(new ControlEvent(token.username, ControlEventType.User_Pool_Sync, matchmakingPool));
                    //Get their info out of the server database.
                    UserInfo userInfo = database.getUserInfo(username);
                    //Tell the rest of the server about their join.
                    lobbyEvent(new LobbyEvent(username, LobbyEventType.User_Connect, userInfo));
                } catch (Exception e) {
                    addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, connection.getConnectionID()));
                    logger.log("Unable to synchronize with a newly connected client.", e, LogLevel.ERROR);
                }
                break;
            case User_Disconnect:
                try {
                    database.invalidateSession((Username) event.getPayload());
                } catch (UserNotFoundException e) {
                    logger.log(e, LogLevel.WARN);
                }
                break;
            case Client_Disconnect:
                connectionPool.disconnect((Integer) event.getPayload());
                break;
        }
    }

    private void controlEvent(ControlEvent event) {
        switch (event.getType()) {
            case Name_Change:
                try {
                    database.changeDisplayName(event.getOrigin(), (String) event.getPayload());
                } catch (UserNotFoundException e) {
                    logger.log(e, LogLevel.WARN);
                }
                break;
            case Pass_Change:
                try {
                    database.changePassword(event.getOrigin(), (Password) event.getPayload());
                } catch (UserNotFoundException e) {
                    logger.log(e, LogLevel.WARN);
                }
                break;
            case Delete_Account:
                try {
                    database.removeAccount(event.getOrigin());
                    addEvent(new ServerEvent(this, ServerEventType.User_Disconnect, event.getOrigin()));
                } catch (UserNotFoundException e) {
                    logger.log(e, LogLevel.WARN);
                }
                break;
            case Server_Disconnect:
            case Pass_Change_Success:
            case Pass_Change_Failure:
            case User_Pool_Sync:
                throw new IllegalStateException();
            case Client_Disconnect:
                addEvent(new ServerEvent(this, ServerEventType.User_Disconnect, event.getOrigin()));
                break;
        }
    }

    private void lobbyEvent(LobbyEvent event) {
        try {
            //Try to execute the event locally.
            matchmakingPool.execute(event);
            Username origin = event.getOrigin();
            Lobby lobby = matchmakingPool.getLobbyList().userGetLobby(origin);
            //Execute any special server-side hooks on lobby events.
            switch(event.getType()) {
                case User_Connect:
                    break;
                case User_Disconnect:
                    break;
                case Lobby_Create:
                    break;
                case Lobby_Change_Config:
                    break;
                case Lobby_Join:
                    if (lobby.getState() == LobbyState.InGame)
                        addEvent(new LobbyEvent(origin, LobbyEventType.Game_Join, null));
                    break;
                case Lobby_Leave:
                    break;
                case Game_Start:
                    GameSettings settings = (GameSettings) event.getPayload();
                    lobby.setGameID(gamePool.start(settings));
                    for (Username username : gamePool.getGamePlayers(lobby.getGameID()).getUsers())
                        addEvent(new LobbyEvent(username, LobbyEventType.Game_Join, null));
                    break;
                case Game_Join:
                    GameProgress progress = gamePool.getGameProgress(lobby.getGameID());
                    LobbyEvent sync = new LobbyEvent(origin, LobbyEventType.Game_Sync, progress);
                    connectionPool.get(origin).sendEvent(sync);
                    break;
                case Game_Leave:
                    break;
                case Game_Sync:
                    break;
                case Game_Finish:
                    gamePool.finish(matchmakingPool.getLobbyList().userGetLobby(origin).getGameID());
                    break;
            }
            //Rebroadcast the event to everyone connected.
            for (Username user : matchmakingPool.getClientList()) {
                ServerConnection client = connectionPool.get(user);
                if (client != null)
                    client.sendEvent(event);
            }
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
        }
    }

    private void gameEvent(GameEvent event) {
        Lobby lobby = matchmakingPool.getLobbyList().userGetLobby(event.getOrigin());
        gamePool.process(lobby.getGameID(), event);
    }

    public void broadcastGameEvent(GameEvent event) {
        Lobby lobby = matchmakingPool.getLobbyList().userGetLobby(event.getOrigin());
        for (Username user : lobby.getConnectedPlayers())
            connectionPool.get(user).sendEvent(event);
    }

    @Override
    public void netEvent(NetEvent event) {
        ServerConnection connection = (ServerConnection) event.getConnection();
        int id = connection.getConnectionID();
        switch (event.getType()) {
            case Register:
                try {
                    //Register the client in the database
                    database.registerAccount((UserLogin) event.getPayload());
                    //Tell the client about the registration success.
                    connection.sendEvent(new NetEvent(token, NetEventType.Register_Success, null));
                } catch (RegistrationException e) {
                    //Tell the client about the auth failure.
                    connection.sendEvent(new NetEvent(token, NetEventType.Register_Failure, e.getMessage()));
                }
                //Kill their connection to the server.
                addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, id));
                break;
            case Log_In:
                try {
                    //Authenticate the client and generate an AuthToken for them.
                    AuthToken clientToken = database.authenticate((UserLogin) event.getPayload());
                    //Tell the client about the auth success, send them their AuthToken.
                    connection.sendEvent(new NetEvent(token, NetEventType.Auth_Success, clientToken));
                    //Synchronize the user and process their join.
                    addEvent(new ServerEvent(connection, ServerEventType.User_Connect, clientToken.username));
                } catch (AuthenticationException | UserNotFoundException e) {
                    //Tell the client about the auth failure.
                    connection.sendEvent(new NetEvent(token, NetEventType.Auth_Failure, e.getMessage()));
                    //Kill their connection to the server.
                    addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, id));
                }
                break;
            case Register_Success:
            case Register_Failure:
            case Auth_Success:
            case Auth_Failure:
                throw new IllegalStateException();
            case Disconnect:
            case Link_Error:
                //Check if the user was logged in
                Username username = connectionPool.getUser(id);
                if (username != null) {
                    // Log the user out.
                    addEvent(new ServerEvent(this, ServerEventType.User_Disconnect, username));
                }
                //Kill their connection to the server.
                addEvent(new ServerEvent(this,ServerEventType.Client_Disconnect,id));
                break;
            case External_Event:
                try {
                    //Validate the incoming user's token.
                    database.validateAuthToken(event.getOrigin());
                    //Forward external events to be handled.
                    addEvent((ExternalEvent) event.getPayload());
                } catch (UserNotFoundException | AuthenticationException e) {
                    logger.log(e, LogLevel.WARN);
                }
                break;
        }
    }

    private void startup(int port) {
        config = ResourceLoader.getPropertiesFile(configFile);
        token = new AuthToken(new Username("Server"), new SecureRandom().nextInt()); //For use in chat and sending events originating here.
        Console console = new Console(this);
        if (GraphicsEnvironment.isHeadless())
            commandLine = new CommandLine(console);
        else
            window = new ServerWindow(this, console);
        connectionPool = new ConnectionPool(this);
        database = new UserDatabase(new PropertiesFileInfo(config.get("database"), "User database"));
        matchmakingPool = new MatchmakingPool();
        gamePool = new GamePool(this);
        try {
            if (port <= 1024) throw new IOException("Port Number Reserved");
            socket = new ServerSocket(port);
            listen = new Thread("Listen") {
                @Override
                public void run() {
                    logger.log("Listening...", LogLevel.INFO);
                    listening = true;
                    while (listening) {
                        try {
                            addEvent(new ServerEvent(this,ServerEventType.Client_Connect,socket.accept()));
                        } catch (SocketException ignored) {
                            listening = false;
                            logger.log("Listening Stopped", LogLevel.INFO);
                        } catch (IOException e) {
                            listening = false;
                            logger.log("Listen Failure", e, LogLevel.WARN);
                        }
                    }
                }
            };
            listen.start();
        } catch (IOException e) {
            logger.log("Server connection failure", e, LogLevel.WARN);
        }
    }

    private void shutdown() {
        database.save();
        listening = false;
        connectionPool.disconnectAll("Server shutting down.");
        try {
            socket.close();
            listen.join();
        } catch (Exception e) {
            logger.log("Shutdown error.", e, LogLevel.WARN);
        }
        try {
            config.save();
        } catch (ConfigurationException e) {
            logger.log("Unable to save configuration.", e, LogLevel.WARN);
        }
        gamePool.join();
        if (commandLine != null) {
            commandLine.close();
        }
        if (window != null) {
            window.dispose();
            window.waitForClose();
        }
        stop();
    }

    public boolean isListening() {
        return listening;
    }

    public String toString() {
        return "Server";
    }
}
