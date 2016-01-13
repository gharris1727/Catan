package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.crypto.Username;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 1/10/16.
 * Pool of clients and their connection info to the server.
 */
public class ClientPool implements Serializable, Iterable<Username>  {
    // Map from username to the information about that user.
    private final Map<Username, UserInfo> clients;
    // Server-side tracking of the client connections.
    private transient final Map<Username, Integer> connectionIDs;
    private transient final Map<Integer, Username> connectedUsers;

    public ClientPool() {
        this.clients = new HashMap<>();
        this.connectionIDs = new HashMap<>();
        this.connectedUsers = new HashMap<>();
    }

    public Iterator<Username> iterator() {
        return clients.keySet().iterator();
    }

    public void storeUserConnection(Username username, int connectionID) {
        connectionIDs.put(username,connectionID);
        connectedUsers.put(connectionID,username);
    }

    public Username getConnectionUsername(int id) {
        return connectedUsers.get(id);
    }

    public int getConnectionID(Username username) {
        if (connectionIDs.containsKey(username))
            return connectionIDs.get(username);
        return 0;
    }

    public void removeUserConnection(Username username) {
        if (connectionIDs.containsKey(username))
            connectedUsers.remove(connectionIDs.remove(username));
    }

    public void removeUserConnection(int connectionID) {
        if (connectedUsers.containsKey(connectionID))
            connectionIDs.remove(connectedUsers.remove(connectionID));
    }

    public boolean hasUser(Username username) {
        return clients.containsKey(username);
    }

    public void updateUserInfo(Username username, UserInfo userInfo) {
        clients.put(username,userInfo);
    }
}
