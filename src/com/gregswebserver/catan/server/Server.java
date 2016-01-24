package com.gregswebserver.catan.server;


import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.crypto.*;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.lobby.MatchmakingPool;
import com.gregswebserver.catan.common.lobby.UserInfo;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.network.ConnectionPool;
import com.gregswebserver.catan.common.network.ServerConnection;
import com.gregswebserver.catan.server.event.ServerEvent;
import com.gregswebserver.catan.server.event.ServerEventType;

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

    private final ServerWindow window;
    private final UserDatabase database;
    private final ConnectionPool connectionPool;
    private final MatchmakingPool matchmakingPool;

    private ServerSocket socket;
    private boolean listening;
    private Thread listen;

    public Server() {
        this(new Logger());
    }

    public Server(Logger logger) {
        super(logger);
        token = new AuthToken(new Username("Server"),new SecureRandom().nextInt()); //For use in chat and sending events originating here.
        window = new ServerWindow(this);
        connectionPool = new ConnectionPool(this);
        database = new UserDatabase(logger);
        matchmakingPool = new MatchmakingPool(this);
        start();
    }

    public void start(int port) {
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
                            Socket clientSocket = socket.accept();
                            connectionPool.connect(clientSocket);
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

    @Override
    protected void externalEvent(ExternalEvent event) {
        if (event instanceof ControlEvent)
            controlEvent((ControlEvent) event);
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
        switch (event.getType()) {
            case Quit_All:
                if (database != null) {
                    database.dumpResources();
                }
                shutdown();
                break;
            case Client_Disconnect:
                connectionPool.closeConnection(matchmakingPool.getClientList().getConnectionID((Username) event.getPayload()));
                matchmakingPool.getClientList().removeUserConnection((Username) event.getPayload());
                break;
            case Client_Connect:
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
            case Client_Pool_Sync:
                throw new IllegalStateException();
            case Client_Disconnect:
                addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, event.getOrigin()));
                break;
            case User_Connect:
            case User_Disconnect:
            case Lobby_Create:
            case Lobby_Change_Config:
            case Lobby_Join:
            case Lobby_Leave:
                try {
                    if (matchmakingPool.test(event))
                        matchmakingPool.execute(event);
                    //Rebroadcast the event to everyone connected.
                    for (Username user : matchmakingPool.getClientList()) {
                        ServerConnection connection = connectionPool.get(matchmakingPool.getClientList().getConnectionID(user));
                        if (connection != null)
                            connection.sendEvent(new NetEvent(token, NetEventType.External_Event, event));
                    }
                } catch (EventConsumerException e) {
                    logger.log(e, LogLevel.ERROR);
                }
                break;
            case Game_Start:
                break;
            case Game_Quit:
                break;
            case Game_End:
                break;
            case Game_Replay:
                break;
            case Replay_Start:
                break;
            case Replay_Quit:
                break;
            case Spectate_Start:
                break;
            case Spectate_Quit:
                break;
        }
    }

    @Override
    public void netEvent(NetEvent event) {
        ServerConnection connection = (ServerConnection) event.getConnection();
        switch (event.getType()) {
            case Log_In:
                logger.log("Authenticating client",LogLevel.DEBUG);
                try {
                    //Log_In the client and generate an AuthToken for them.
                    AuthToken clientToken = database.authenticate((UserLogin) event.getPayload());
                    //Tell the client about the auth success, send them their AuthToken.
                    connection.sendEvent(new NetEvent(token, NetEventType.Log_In_Success, clientToken));
                    //Send them a sync of the current client pool state.
                    connection.sendEvent(new NetEvent(token,NetEventType.External_Event,
                            new ControlEvent(token.username,ControlEventType.Client_Pool_Sync, matchmakingPool)));
                    //Get their info out of the server database.
                    UserInfo userInfo = database.getUserInfo(clientToken.username);
                    //Keep track of their connection id in the database.
                    matchmakingPool.getClientList().storeUserConnection(clientToken.username,connection.getConnectionID());
                    //Tell the rest of the server about their join.
                    externalEvent(new ControlEvent(clientToken.username,ControlEventType.User_Connect,userInfo));
                } catch (AuthenticationException | UserNotFoundException e) {
                    logger.log(e,LogLevel.WARN);
                    //Tell the client about the auth failure.
                    connection.sendEvent(new NetEvent(token, NetEventType.Log_In_Failure, e.getMessage()));
                    //Kill their connection to the server.
                    int id = connection.getConnectionID();
                    addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, id));
                }
                break;
            case Log_In_Success:
            case Log_In_Failure:
                throw new IllegalStateException();
            case Disconnect:
            case Link_Error:
                int id = connection.getConnectionID();
                //Check if the user was logged in
                Username username = matchmakingPool.getClientList().getConnectionUsername(id);
                if (username != null) {
                    // Log the user out.
                    addEvent(new ControlEvent(username,ControlEventType.User_Disconnect,null));
                }
                //Kill their connection to the server.
                addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, username));
                break;
            case External_Event:
                //Forward external events to be handled.
                externalEvent((ExternalEvent) event.getPayload());
                break;
        }
    }

    public void shutdown() {
        window.dispose();
        listening = false;
        connectionPool.disconnectAll("Server closed");
        try {
            socket.close();
            listen.join();
        } catch (Exception e) {
            logger.log("Shutdown error.", e, LogLevel.WARN);
        }
        stop();
    }

    public String toString() {
        return "Server";
    }

}
