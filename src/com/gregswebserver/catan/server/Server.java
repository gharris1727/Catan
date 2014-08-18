package com.gregswebserver.catan.server;


import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.chat.ChatEvent;
import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.event.GenericEvent;
import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.event.ThreadStop;
import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.network.ServerConnection;

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
            switch (((GameEvent) event).type) {
                case Game_Create:
                    break;
                case Player_Join:
                    break;
                case Player_Leave:
                    break;
                case Player_Build_Settlement:
                    break;
                case Player_Build_City:
                    break;
                case Player_Build_Road:
                    break;
                case Player_Move_Robber:
                    break;
                case Player_Roll_Dice:
                    break;
                case Player_Offer_Trade:
                    break;
                case Player_Accept_Trade:
                    break;
                case Player_Make_Trade:
                    break;
            }
        }
        if (event instanceof ServerEvent) {

        }
    }

    public void shutdown() {
        for (ServerConnection connection : connections) {
            connection.disconnect();
        }
        listening = false;
        try {
            socket.close();
        } catch (IOException e) {
            logger.log("ServerSocket Close error", e, LogLevel.WARN);
        }
        stop();
    }
}
