package com.gregswebserver.catan.network;

import com.gregswebserver.catan.server.Server;

import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetworkConnection {

    private Server server;

    public ServerConnection(Socket socket, Server server) {
        super(socket, Server.logger);
        this.server = server;
    }

    public void receive(Object o) {
        //TODO: add object processing to the server
    }

}

