package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.client.Client;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetConnection<Client> {

    public ClientConnection(Client client, Identity identity) {
        super(client, identity);
    }

    public void connectTo(NetID id) {
        setNetID(id);
        connect();
    }
}
