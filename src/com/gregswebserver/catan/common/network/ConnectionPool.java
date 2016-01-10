package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.NetEvent;
import com.gregswebserver.catan.common.event.NetEventType;
import com.gregswebserver.catan.server.Server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Greg on 12/27/2014.
 * Class for managing a collection of clients all clients to the server.
 */
public class ConnectionPool implements Iterable<ServerConnection> {

    private final Server server;
    private final HashMap<Integer, ServerConnection> connections;
    private int totalClients;
    private int disconnectedClients;

    public ConnectionPool(Server server) {
        this.server = server;
        connections = new HashMap<>();
        totalClients = 0;
        disconnectedClients = 0;
    }

    public void connect(Socket socket) {
        ServerConnection connection = new ServerConnection(server, socket, ++totalClients);
        connections.put(totalClients, connection);
        connection.connect();
    }

    public ServerConnection get(int connectionID) {
        return connections.get(connectionID);
    }

    public void disconnect(int connectionID, String reason) {
        if (connections.containsKey(connectionID)) {
            ServerConnection conn = connections.remove(connectionID);
            conn.sendEvent(new NetEvent(server.getToken(), NetEventType.Disconnect, reason));
            conn.disconnect();
            disconnectedClients++;
        }
    }

    public void disconnectAll(String reason) {
        for (ServerConnection conn : connections.values()) {
            conn.sendEvent(new NetEvent(server.getToken(), NetEventType.Disconnect, reason));
            conn.disconnect();
            disconnectedClients++;
        }
    }

    public void closeConnection(int connectionID) {
        if (connections.containsKey(connectionID)) {
            connections.remove(connectionID).disconnect();
            disconnectedClients++;
        }
    }

    public Iterator<ServerConnection> iterator() {
        return connections.values().iterator();
    }

    public int size() {
        return totalClients - disconnectedClients;
    }
}
