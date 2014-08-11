package com.gregswebserver.catan.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multiplayer over internet.
 */
public class Server extends Thread {

    private final int MAX_CONNECTIONS = 10;
    private int port;
    private ArrayList<Client> clients;
    private ServerSocket socket;
    private boolean running;

    public Server(int port) throws IOException {
        if (port <= 1024) {
            throw new IllegalArgumentException("Port " + port + " is reserved!");
        }
        this.port = port;
        clients = new ArrayList<>();
        socket = new ServerSocket(port);
        running = true;
    }

    public void start() {

    }

    public void run() {
        try {
            while (clients.size() < MAX_CONNECTIONS) {
                Socket clientSocket = socket.accept();
            }
        } catch (Exception e) {

        }

    }
}
