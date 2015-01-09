package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.server.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection {

    public ServerConnection(Server server, Socket socket, int connectionID) {
        super(server);
        this.socket = socket;
        local = new NetID(socket);
        remote = new NetID((InetSocketAddress) socket.getRemoteSocketAddress());
    }

    public void run() {
        logger.log("Connecting to new Client...", LogLevel.INFO);
        try {
            open = true;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            logger.log("Client Connection Established.", LogLevel.DEBUG);
            receive.start(); //Start processing objects after the connection is established.
        } catch (Exception e) {
            open = false;
            logger.log("Connect Failure", e, LogLevel.ERROR);
        }
    }
}

