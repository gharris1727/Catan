package catan.server.structure;

import catan.common.crypto.Username;
import catan.common.log.Logger;
import catan.common.network.NetEventType;
import catan.common.network.ServerConnection;
import catan.server.Server;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Greg on 12/27/2014.
 * Class for managing a collection of clients all clients to the server.
 */
public class ConnectionPool {

    private final Server server;
    private final Map<Integer, ServerConnection> connections;
    private final Map<Username, Integer> usernameConnectionIDs;
    private final Map<Integer, Username> connectionUsers;
    private int nextID = 1;

    public ConnectionPool(Server server) {
        this.server = server;
        connections = new HashMap<>();
        usernameConnectionIDs = new HashMap<>();
        connectionUsers = new HashMap<>();
    }

    public ServerConnection startConnection(Logger logger, Socket socket) {
        ServerConnection connection = new ServerConnection(server, logger, socket, nextID++);
        connections.put(connection.getConnectionID(), connection);
        return connection;
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
        if ((id != null) && connections.containsKey(id))
            connections.get(id).sendEvent(NetEventType.Disconnect, reason);
    }

    public void disconnect(int connectionID) {
        ServerConnection connection = connections.remove(connectionID);
        if ((connection != null) && connection.isOpen())
            connection.disconnect();
    }

    public void disconnectAll(String reason) {
        Map<Username, Integer> connections = new HashMap<>(usernameConnectionIDs);
        for (Entry<Username, Integer> entry : connections.entrySet()) {
            disconnectUser(entry.getKey(), reason);
            disconnect(entry.getValue());
        }
    }
}
