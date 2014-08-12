package com.gregswebserver.catan.server;


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
public class Server {

    public static Logger logger;
    private final int MAX_CONNECTIONS = 1000;
    private int port;
    private ArrayList<ServerConnection> connections;
    private ServerSocket socket;
    private Thread listen, console;
    private ServerWindow window;
    private Server instance;

    public Server(int port) {
        instance = this;
        this.port = port;
        connections = new ArrayList<>();
        startConsole();
        openSocket();
        listen();
    }

    public void openSocket() {
        try {
            if (port <= 1024) throw new IOException("Port Number Reserved");
            socket = new ServerSocket(port);
        } catch (IOException e) {
            logger.log("Server connection failure", e, LogLevel.ERROR);
        }
    }

    public void listen() {
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
        listen.start();
    }

    private void startConsole() {
        console = new Thread("Console") {
            public void run() {
                logger.log("Starting Console...", LogLevel.INFO);
                window = new ServerWindow();
            }
        };
        console.start();
    }
}
