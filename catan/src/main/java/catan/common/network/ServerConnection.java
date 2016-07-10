package catan.common.network;

import catan.server.Server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Greg on 8/11/2014.
 * A client as represented on the server side application.
 */
public class ServerConnection extends NetConnection {

    private static final Object nextIDLock = new Object();
    private static int nextID = 1;

    private final int connectionID;

    public ServerConnection(Server server, Socket socket) {
        super(server);
        synchronized (nextIDLock) {
            this.connectionID = nextID++;
        }
        this.socket = socket;
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
            onError("Connect", e);
        }
    }

    public int getConnectionID() {
        return connectionID;
    }
}

