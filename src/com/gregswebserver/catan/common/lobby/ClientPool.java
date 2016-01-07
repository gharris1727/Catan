package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.ControlEvent;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.crypto.Username;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Greg on 12/29/2014.
 * Pool of clients
 */
public class ClientPool extends EventPayload implements EventConsumer<ControlEvent>, Iterable<ServerClient> {

    private final HashMap<Username, ServerClient> clients; //Maps identities to the server clients.
    private final HashMap<Username, Lobby> lobbies; //Maps Users to their lobbies.

    public ClientPool() {
        clients = new HashMap<>();
        lobbies = new HashMap<>();
    }

    public Iterator<ServerClient> iterator() {
        return clients.values().iterator();
    }

    public boolean test(ControlEvent event) {
        Username origin = event.getOrigin();
        boolean exists = clients.containsKey(origin);
        boolean inLobby = exists && lobbies.containsKey(origin);
        switch (event.getType()) {
            case Client_Connect:
                return !exists;
            case Client_Disconnect:
                return exists;
            case Lobby_Create:
                return exists && !inLobby;
            case Lobby_Change_Config:
            case Lobby_Change_Owner:
            case Lobby_Delete:
                //Must be owner of lobby.
                return inLobby && lobbies.get(origin).getOwner().equals(origin);
            case Lobby_Join:
                //Lobby in payload must exist.
                return exists && !inLobby && lobbies.containsKey(event.getPayload());
            case Lobby_Leave:
                return inLobby;
            default:
                return false;
        }
    }

    public void execute(ControlEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        Username origin = event.getOrigin();
        ServerClient client;
        Username username;
        Lobby lobby;
        switch (event.getType()) {
            case Name_Change:
                client = clients.get(event.getOrigin());
                client.setDisplayName((String) event.getPayload());
            case Client_Connect:
                client = (ServerClient) event.getPayload();
                clients.put(origin, client);
                break;
            case Client_Disconnect:
                username = (Username) event.getPayload();
                clients.remove(username);
                break;
            case Lobby_Create:
                lobby = new Lobby(origin);
                lobbies.put(origin, lobby);
                break;
            case Lobby_Change_Config:
                lobby = lobbies.get(event.getOrigin());
                lobby.setConfig((LobbyConfig) event.getPayload());
                break;
            case Lobby_Change_Owner:
                lobby = lobbies.get(event.getOrigin());
                lobby.setOwner((Username) event.getPayload());
                break;
            case Lobby_Delete:
                lobby = lobbies.get(event.getOrigin());
                for (Username clientID : lobby)
                    lobbies.remove(clientID);
                break;
            case Lobby_Join:
                username = (Username) event.getPayload();
                lobby = lobbies.get(username);
                lobby.add(origin);
                lobbies.put(origin, lobby);
                break;
            case Lobby_Leave:
                lobby = lobbies.remove(event.getOrigin());
                lobby.remove(event.getOrigin());
                break;
        }
    }

    public int getConnectionID(Username username) {
        if (clients.containsKey(username)) {
            ServerClient client = clients.get(username);
            return client.getUniqueID();
        }
        return 0;
    }

    public Lobby getLobby(Username username) {
        return lobbies.get(username);
    }

    public String toString() {
        return "ClientPool";
    }
}
