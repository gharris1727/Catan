package com.gregswebserver.catan.network;

import com.gregswebserver.catan.client.Client;

import java.net.InetAddress;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetConnection {

    private Client client;

    public ClientConnection(InetAddress host, int port, Client client) {
        super(host, port, client.logger);
        this.client = client;
        connect();
    }

    public void getEvent(NetEvent e) {
        client.addEvent(e);
    }
}
