package com.gregswebserver.catan.server;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.common.crypto.Authenticator;
import com.gregswebserver.catan.common.crypto.UserLogin;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.NetID;
import com.gregswebserver.catan.server.client.ConnectionPool;
import com.gregswebserver.catan.server.client.ServerClient;
import com.gregswebserver.catan.server.event.ServerEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends QueuedInputThread<GenericEvent> {

    private final Identity identity;
    private final ServerWindow window;
    private ConnectionPool connectionPool;

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
    }

    public void start(int port) {
        try {
            if (port <= 1024) throw new IOException("Port Number Reserved");
            socket = new ServerSocket(port);
            listen = new Thread("Listen") {
                public void run() {
                    logger.log("Listening...", LogLevel.INFO);
                    try {
                        listening = true;
                        while (listening) {
                            Socket clientSocket = socket.accept();
                            connectionPool.processNewConnection(clientSocket);
                        }
                    } catch (Exception e) {
                        logger.log("Listen Failure", e, LogLevel.ERROR);
                    }

                }
            };
            listen.start();
        } catch (IOException e) {
            logger.log("Server connection failure", e, LogLevel.ERROR);
        }
    }

    public void execute() throws ThreadStop {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
        if (event instanceof InternalEvent) {
            internalEvent((InternalEvent) event);
        } else if (event instanceof ExternalEvent) {
            externalEvent((ExternalEvent) event);
        } else {
            logger.log("Received invalid GenericEvent", LogLevel.WARN);
            //Something broke?
        }
    }

    private void internalEvent(InternalEvent event) {
        if (event instanceof ServerEvent) {
            switch (((ServerEvent) event).getType()) {
                case Quit_All:
                    shutdown();
                    break;
                case Client_Login:
                    connectionPool.processNewClient((ServerClient) event.getPayload());
                    break;
            }
        } else {
            logger.log("Received invalid InternalEvent", LogLevel.WARN);
        }
    }

    private void externalEvent(ExternalEvent event) {

    }


    public void shutdown() {
        connectionPool.disconnectAll();
        window.dispose();
        listening = false;
        try {
            socket.close();
        } catch (IOException e) {
            logger.log("ServerSocket Close error", e, LogLevel.WARN);
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
}
