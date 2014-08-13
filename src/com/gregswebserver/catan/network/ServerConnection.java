package com.gregswebserver.catan.network;

import com.gregswebserver.catan.server.Server;

import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection {

    private Server server;

    public ServerConnection(Socket socket, Server server) {
        super(socket, server.logger);
        this.server = server;
    }

    public void getEvent(NetEvent e) {
        server.addEvent(e);
    }

}

