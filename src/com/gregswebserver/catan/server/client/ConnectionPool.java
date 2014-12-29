package com.gregswebserver.catan.server.client;

import com.gregswebserver.catan.common.lobby.Lobby;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.ServerConnection;
import com.gregswebserver.catan.server.Server;

import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Greg on 12/27/2014.
 * Class for managing a collection of clients all clients to the server.
 */
public class ConnectionPool {

    private final HashMap<Integer, ServerConnection> connections;
    private final HashMap<Identity, ServerClient> clients;
    private final HashMap<Identity, Lobby> lobbies;
    private Server server;
    private int totalClients;
    private int disconnectedClients;

    public ConnectionPool(Server server) {
        this.server = server;
        connections = new HashMap<>();
        clients = new HashMap<>();
        lobbies = new HashMap<>();
        totalClients = 0;
        disconnectedClients = 0;
    }

    public void disconnectAll() {
        for (ServerConnection conn : connections.values()) {
            conn.disconnect();
            disconnectedClients++;
        }
        connections.clear();
        clients.clear();
        lobbies.clear();
    }

    public void processNewConnection(Socket clientSocket) {
        ServerConnection newClient = new ServerConnection(server, clientSocket, ++totalClients);
        connections.put(totalClients, newClient);
        newClient.connect();
    }

    public void processNewClient(ServerClient client) {
        clients.put(client.getIdentity(), client);
    }

    public void disconnectClient(int uniqueID) {
        if (connections.containsKey(uniqueID)) {
            ServerConnection conn = connections.remove(uniqueID);
            conn.disconnect();
            disconnectedClients++;
        }
    }

    public int size() {
        return totalClients - disconnectedClients;
    }
}
