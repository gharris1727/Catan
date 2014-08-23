package com.gregswebserver.catan.network;

import com.gregswebserver.catan.client.Client;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetConnection {
    private Client client;

    public ClientConnection(Client client, Identity identity) {
        super(client.logger);
        this.client = client;
        this.identity = identity;
    }

    public void connectTo(NetID id) {
        setNetID(id);
        connect();
    }

    public void process(NetEvent e) {
        client.addEvent(e.event);
    }
}
