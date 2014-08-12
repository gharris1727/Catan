package com.gregswebserver.catan.client;

import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.network.ClientConnection;

import java.net.InetAddress;

/**
 * Created by Greg on 8/11/2014.
 * Game client handling user input, graceful error handling, local game simulation, and communication to a server.
 */
public class Client {

    public static Logger logger;
    private Client instance;
    private ClientWindow window;
    private ClientConnection connection;

    public Client() {
        instance = this;
        window = new ClientWindow();
    }

    public void connect(InetAddress host, int port) {
        connection = new ClientConnection(host, port, instance);
    }

    public void login(String username, String password) {
        //TODO: add login authentication.
    }
}
