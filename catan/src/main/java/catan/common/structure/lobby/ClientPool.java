package catan.common.structure.lobby;

import catan.common.crypto.Username;
import catan.common.event.EventPayload;
import catan.common.structure.UserInfo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 1/10/16.
 * List of clients for use in sending over the network.
 */
public class ClientPool extends EventPayload implements Iterable<Username>  {

    // Map from username to the information about that user.
    private final Map<Username, UserInfo> clients;

    public ClientPool() {
        clients = new HashMap<>();
    }

    @Override
    public Iterator<Username> iterator() {
        return clients.keySet().iterator();
    }

    public boolean hasUser(Username username) {
        return clients.containsKey(username);
    }

    public void add(Username username, UserInfo userInfo) {
        clients.put(username,userInfo);
    }

    public void remove(Username username) {
        clients.remove(username);
    }
}
