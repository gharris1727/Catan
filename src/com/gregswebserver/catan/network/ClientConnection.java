package com.gregswebserver.catan.network;

import com.gregswebserver.catan.client.Client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetworkConnection {

    private int port;
    private InetAddress host;
    private Client client;
    private Socket socket;
    private Thread receive;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean open;

    public ClientConnection(InetAddress host, int port, Client client) {
        super(host, port);
        this.client = client;
        connect();
    }

    public void receive(Object o) {
        //TODO: send data from the ClientConnection to the Client
    }

}
