package com.gregswebserver.catan.server;


import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.crypto.AuthenticationException;
import com.gregswebserver.catan.common.crypto.Password;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.InternalEvent;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.network.NetEvent;
import com.gregswebserver.catan.common.network.NetEventType;
import com.gregswebserver.catan.common.network.ServerConnection;
import com.gregswebserver.catan.common.structure.UserInfo;
import com.gregswebserver.catan.common.structure.UserLogin;
import com.gregswebserver.catan.common.structure.event.ControlEvent;
import com.gregswebserver.catan.common.structure.event.ControlEventType;
import com.gregswebserver.catan.common.structure.event.LobbyEvent;
import com.gregswebserver.catan.common.structure.event.LobbyEventType;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.common.structure.lobby.MatchmakingPool;
import com.gregswebserver.catan.server.event.ServerEvent;
import com.gregswebserver.catan.server.event.ServerEventType;
import com.gregswebserver.catan.server.structure.ConnectionPool;
import com.gregswebserver.catan.server.structure.GamePool;
import com.gregswebserver.catan.server.structure.UserDatabase;
import com.gregswebserver.catan.server.structure.UserNotFoundException;

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

    private ServerWindow window;
    private UserDatabase database;
    private ConnectionPool connectionPool;
    private MatchmakingPool matchmakingPool;
    private GamePool gamePool;

    private ServerSocket socket;
    private boolean listening;
    private Thread listen;

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
                connectionPool.disconnectUser((Username) event.getPayload(), "Server-side Disconnect");
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
                    break;
                case Lobby_Leave:
                    break;
                case Lobby_Start:
                    gamePool.start((GameSettings) event.getPayload());
                    break;
                case Lobby_Finish:
                    gamePool.finish(((Username) event.getPayload()));
                    break;
            }
            //Rebroadcast the event to everyone connected.
            for (Username user : matchmakingPool.getClientList())
                connectionPool.get(user).sendEvent(event);
        } catch (Exception e) {
            logger.log(e, LogLevel.ERROR);
        }
    }

    private void gameEvent(GameEvent event) {
        gamePool.process(event);
        for (Username user : matchmakingPool.getLobbyList().userGetLobby(event.getOrigin()).getUsers())
            connectionPool.get(user).sendEvent(event);
    }

    @Override
    public void netEvent(NetEvent event) {
        ServerConnection connection = (ServerConnection) event.getConnection();
        int id = connection.getConnectionID();
        switch (event.getType()) {
            case Log_In:
                logger.log("Authenticating client",LogLevel.DEBUG);
                try {
                    //Authenticate the client and generate an AuthToken for them.
                    AuthToken clientToken = database.authenticate((UserLogin) event.getPayload());
                    //Tell the client about the auth success, send them their AuthToken.
                    connection.sendEvent(new NetEvent(token, NetEventType.Log_In_Success, clientToken));
                    //Synchronize the user and process their join.
                    addEvent(new ServerEvent(connection, ServerEventType.User_Connect, clientToken.username));
                } catch (AuthenticationException | UserNotFoundException e) {
                    //Tell the client about the auth failure.
                    connection.sendEvent(new NetEvent(token, NetEventType.Log_In_Failure, e.getMessage()));
                    //Kill their connection to the server.
                    addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, id));
                }
                break;
            case Log_In_Success:
            case Log_In_Failure:
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
        token = new AuthToken(new Username("Server"), new SecureRandom().nextInt()); //For use in chat and sending events originating here.
        window = new ServerWindow(this);
        connectionPool = new ConnectionPool(this);
        database = new UserDatabase(logger);
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
        window.dispose();
        listening = false;
        connectionPool.disconnectAll("Server shutting down.");
        try {
            socket.close();
            listen.join();
        } catch (Exception e) {
            logger.log("Shutdown error.", e, LogLevel.WARN);
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
