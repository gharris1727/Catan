package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.server.Server;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Greg on 12/27/2014.
 * Class for managing a collection of clients all clients to the server.
 */
public class ConnectionPool {

    private final HashMap<Integer, ServerConnection> connections;
    private Server server;
    private int totalClients;
    private int disconnectedClients;

    public ConnectionPool(Server server) {
        this.server = server;
        connections = new HashMap<>();
        totalClients = 0;
        disconnectedClients = 0;
    }

    public void disconnectAll(String reason) {
        for (ServerConnection conn : connections.values()) {
            conn.sendEvent(new ControlEvent(server.getIdentity(), ControlEventType.Server_Disconnect, reason));
            conn.disconnect();
            disconnectedClients++;
        }
        connections.clear();
    }

    public void processNewConnection(Socket clientSocket) {
        ServerConnection newClient = new ServerConnection(server, clientSocket, ++totalClients);
        connections.put(totalClients, newClient);
        newClient.connect();
    }

    public void disconnectClient(int uniqueID, String reason) {
        if (connections.containsKey(uniqueID)) {
            ServerConnection conn = connections.remove(uniqueID);
            conn.sendEvent(new ControlEvent(server.getIdentity(), ControlEventType.Server_Disconnect, reason));
            conn.disconnect();
            disconnectedClients++;
        }
    }

    public boolean sendClientEvent(int uniqueID, ExternalEvent event) {
        if (connections.containsKey(uniqueID)) {
            connections.get(uniqueID).sendEvent(event);
            return true;
        }
        return false;
    }

    public int size() {
        return totalClients - disconnectedClients;
    }
}
