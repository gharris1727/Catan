package com.gregswebserver.catan.server;


import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.network.ServerConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multi-player over internet.
 */
public class Server extends QueuedInputThread<GenericEvent> {

    private final int MAX_CONNECTIONS = 1000;
    private ArrayList<ServerConnection> connections;
    private ServerSocket socket;
    private Thread listen;
    private ServerWindow window;
    private Server instance;

    public Server() {
        super(new Logger());
        instance = this;
        connections = new ArrayList<>();
        window = new ServerWindow(this);
        listen = new Thread("Listen") {
            public void run() {
                logger.log("Listening...", LogLevel.INFO);
                try {
                    while (connections.size() < MAX_CONNECTIONS) {
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

    public void execute() {
        //Process events from the input queue.
        GenericEvent event = getEvent(true);
        if (event instanceof ChatEvent) {
            //TODO: Process events
        }
        if (event instanceof GameEvent) {
        }
    }

    public void shutdown() {
        //Close all pertinant threads and shut down the server.
        //TODO: implement shutdown.
    }
}
