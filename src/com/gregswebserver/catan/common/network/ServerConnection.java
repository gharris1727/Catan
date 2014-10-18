package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.server.Server;

import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection<Server> {

    public ServerConnection(Server server, Socket socket) {
        super(server, new ConnectionInfo(server.getIdentity(), null, new NetID(socket)));
    }
}

