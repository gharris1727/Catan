package com.gregswebserver.catan.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Greg on 8/9/2014.
 * Server architecture to allow for multiplayer over internet.
 */
public class Server extends Thread {

    private int port;
    private ServerSocket socket;
    private boolean running;

    public Server(int port) throws IOException {
        if (port <= 1024) {
            throw new IllegalArgumentException("Port " + port + " is reserved!");
        }
        this.port = port;
        socket = new ServerSocket(port);
        running = true;
    }

    public void run() {

    }
}
