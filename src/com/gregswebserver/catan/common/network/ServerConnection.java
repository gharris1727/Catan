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

    private int connectionID;

    public ServerConnection(Server server, Socket socket, int connectionID) {
        super(server);
        this.socket = socket;
        this.connectionID = connectionID;
        local = new NetID(socket);
        remote = new NetID((InetSocketAddress) socket.getRemoteSocketAddress());
    }

    @Override
    public void run() {
        try {
            open = true;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            receive.start(); //Start processing objects after the connection is established.
        } catch (Exception e) {
            onDisconnect("Connect failure: " + e.getMessage() + ".");
            logger.log("Connect Failure", e, LogLevel.ERROR);
        }
    }

    public int getConnectionID() {
        return connectionID;
    }
}

