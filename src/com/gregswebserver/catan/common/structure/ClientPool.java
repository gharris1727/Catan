package com.gregswebserver.catan.common.structure;

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

    public ClientPool() {
        this.clients = new HashMap<>();
    }

    @Override
    public Iterator<Username> iterator() {
        return clients.keySet().iterator();
    }

    public boolean hasUser(Username username) {
        return clients.containsKey(username);
    }

    public void updateUserInfo(Username username, UserInfo userInfo) {
        clients.put(username,userInfo);
    }
}
