package com.gregswebserver.catan.server;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.network.ServerConnection;
import com.gregswebserver.catan.server.lobby.Lobby;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends QueuedInputThread {

    private ArrayList<ServerConnection> connections;
    private ArrayList<Lobby> lobbies;
    private ServerSocket socket;
    private Thread listen;
    private boolean listening;
    private ServerWindow window;
    private Server instance;

    public Server() {
        super(Main.logger); //TODO: REMOVE ME!
//        super(new Logger());
        instance = this;
        connections = new ArrayList<>();
        window = new ServerWindow(this);
        listen = new Thread("Listen") {
            public void run() {
                logger.log("Listening...", LogLevel.INFO);
                try {
                    listening = true;
                    while (listening) {
                        Socket clientSocket = socket.accept();
                        ServerConnection newClient = new ServerConnection(clientSocket, instance);
                        connections.add(newClient);
                    }
                } catch (Exception e) {
                    logger.log("Listen Failure", e, LogLevel.ERROR);
                }

            }
        };
    }

    public void start(int port) {
        try {
            if (port <= 1024) throw new IOException("Port Number Reserved");
            socket = new ServerSocket(port);
            listen.start();
        } catch (IOException e) {
            logger.log("Server connection failure", e, LogLevel.ERROR);
        }
    }

    public void execute() throws ThreadStop {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
        if (event instanceof ChatEvent) {

        }
        if (event instanceof GameEvent) {

        }
        if (event instanceof ServerEvent) {
            switch (((ServerEvent) event).type) {
                case Client_Connect:
                    break;
                case Client_Disconnect:
                    break;
                case Client_Join:
                    break;
                case Client_Leave:
                    break;
                case Lobby_Create:
                    Lobby lobby = new Lobby(this, ((ServerEvent) event).origin);
                    break;
                case Lobby_Delete:
                    break;
                case Lobby_Join:
                    break;
                case Lobby_Leave:
                    break;
            }
        }
    }

    public void shutdown() {
        for (ServerConnection connection : connections) {
            connection.disconnect();
        }
        window.dispose();
        listening = false;
        try {
            socket.close();
        } catch (IOException e) {
            logger.log("ServerSocket Close error", e, LogLevel.WARN);
        }
        stop();
    }

    public String toString() {
        return "Server";
    }
}
