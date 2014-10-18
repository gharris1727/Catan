package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;

/**
 * Created by Greg on 8/10/2014.
 * Game client that includes graphics, networking, and playing catan games.
 */
public class ClientConnection extends NetConnection<Client> {

    public ClientConnection(Client client, ConnectionInfo info) {
        super(client, info);
    }
}
