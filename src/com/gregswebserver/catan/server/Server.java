package com.gregswebserver.catan.server;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.common.crypto.Authenticator;
import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.lobby.ClientPool;
import com.gregswebserver.catan.common.lobby.ServerClient;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.network.ConnectionPool;
import com.gregswebserver.catan.common.network.ControlEvent;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.NetID;
import com.gregswebserver.catan.server.event.ServerEvent;
import com.gregswebserver.catan.server.event.ServerEventType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends QueuedInputThread<GenericEvent> {

    private final Identity identity;
    private final ServerWindow window;
    private ConnectionPool connectionPool;
    private ClientPool clientPool;

    private ServerSocket socket;
    private boolean listening;
    private Thread listen;
    private Authenticator authenticator;

    public Server() {
        super(Main.logger); //TODO: Create new logger for the server.
        identity = new Identity("Server"); //For use in chat and sending events originating here.
        window = new ServerWindow(this);
        connectionPool = new ConnectionPool(this);
        authenticator = new Authenticator(this);
        clientPool = new ClientPool();
    }

    public void start(int port) {
        try {
            if (port <= 1024) throw new IOException("Port Number Reserved");
            socket = new ServerSocket(port);
            listen = new Thread("Listen") {
                public void run() {
                    logger.log("Listening...", LogLevel.INFO);
                    listening = true;
                    while (listening) {
                        try {
                            Socket clientSocket = socket.accept();
                            connectionPool.processNewConnection(clientSocket);
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

    public void execute() throws ThreadStop {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
//        logger.log("Server received event: " + event, LogLevel.DEBUG);
        if (event instanceof InternalEvent) {
            if (event instanceof ServerEvent) {
                serverEvent((ServerEvent) event);
            } else {
                logger.log("Received invalid InternalEvent", LogLevel.ERROR);
            }
        } else if (event instanceof ExternalEvent) {
            if (event instanceof ControlEvent) {
                controlEvent((ControlEvent) event);
            } else {
                logger.log("Received invalid ExternalEvent", LogLevel.ERROR);
            }
        } else {
            logger.log("Received invalid GenericEvent", LogLevel.ERROR);
        }
    }

    private void serverEvent(ServerEvent event) {
        switch (event.getType()) {
            case Quit_All:
                shutdown();
                break;
            case Client_Disconnect:
                connectionPool.disconnectClient((Integer) event.getPayload(), "Disconnected");
                break;
        }
    }

    private void controlEvent(ControlEvent event) {
        switch (event.getType()) {
            case Pass_Change:
                authenticator.changeLogin(new UserLogin(event.getOrigin(), (String) event.getPayload()));
                break;
            case Handshake_Client_Connect:
            case Handshake_Client_Connect_Success:
            case Handshake_Client_Connect_Failure:
            case Server_Disconnect:
            case Pass_Change_Success:
            case Pass_Change_Failure:
                //Should die in the client, and never get here.
                break;
            case Client_Disconnect:
                int uniqueID = clientPool.getUniqueID(event.getOrigin());
                addEvent(new ServerEvent(this, ServerEventType.Client_Disconnect, uniqueID));
            case Client_Connect:
            case Lobby_Create:
            case Lobby_Change_Config:
            case Lobby_Delete:
            case Lobby_Join:
            case Lobby_Leave:
                try {
                    if (clientPool.test(event))
                        clientPool.execute(event);
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

    public void sendToLobby(Identity identity, ExternalEvent event) {
        for (Identity i : clientPool.getLobby(identity)) {
            int uniqueID = clientPool.getUniqueID(i);
            connectionPool.sendClientEvent(uniqueID, event);
        }
    }

    public void sendToAll(ExternalEvent event) {
        for (ServerClient client : clientPool)
            connectionPool.sendClientEvent(client.getUniqueID(), event);
    }

    public void sendToClient(Identity identity, ExternalEvent event) {
        int uniqueID = clientPool.getUniqueID(identity);
        connectionPool.sendClientEvent(uniqueID, event);
    }


    public void shutdown() {
        connectionPool.disconnectAll("Server Closed");
        window.dispose();
        listening = false;
        try {
            socket.close();
            listen.join();
        } catch (Exception e) {
            logger.log("Shutdown error.", e, LogLevel.WARN);
        }
        stop();
    }

    public Identity getIdentity() {
        return identity;
    }

    public String toString() {
        return identity.username;
    }

    public boolean authenticate(NetID remote, UserLogin login) {
        return authenticator.authLogin(login);
    }

    public ClientPool getClientPool() {
        return clientPool;
    }
}
