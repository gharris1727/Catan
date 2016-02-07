package com.gregswebserver.catan.server.structure;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.network.NetEvent;
import com.gregswebserver.catan.common.network.NetEventType;
import com.gregswebserver.catan.common.network.ServerConnection;
import com.gregswebserver.catan.server.Server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 12/27/2014.
 * Class for managing a collection of clients all clients to the server.
 */
public class ConnectionPool {

    private final Server server;
    private final Map<Integer, ServerConnection> connections;
    private final Map<Username, Integer> usernameConnectionIDs;
    private final Map<Integer, Username> connectionUsers;

    public ConnectionPool(Server server) {
        this.server = server;
        connections = new HashMap<>();
        usernameConnectionIDs = new HashMap<>();
        connectionUsers = new HashMap<>();
    }

    public void addConnection(ServerConnection connection) {
        connections.put(connection.getConnectionID(), connection);
    }

    public void addUser(Username username, ServerConnection connection) {
        usernameConnectionIDs.put(username, connection.getConnectionID());
        connectionUsers.put(connection.getConnectionID(), username);
    }

    public Username getUser(int id) {
        return connectionUsers.get(id);
    }

    public ServerConnection get(int connectionID) {
        return connections.get(connectionID);
    }

    public ServerConnection get(Username username) {
        return connections.get(usernameConnectionIDs.get(username));
    }

    public void disconnectUser(Username username, String reason) {
        Integer id = usernameConnectionIDs.remove(username);
        connectionUsers.remove(id);
        if (id != null && connections.containsKey(id))
            connections.get(id).sendEvent(new NetEvent(server.getToken(), NetEventType.Disconnect, reason));
    }

    public void disconnect(int connectionID) {
        ServerConnection connection = connections.remove(connectionID);
        if (connection != null && connection.isOpen())
            connection.disconnect();
    }

    public void disconnectAll(String reason) {
        for (Map.Entry<Username, Integer> entry : usernameConnectionIDs.entrySet()) {
            disconnectUser(entry.getKey(), reason);
            disconnect(entry.getValue());
        }
    }
}
