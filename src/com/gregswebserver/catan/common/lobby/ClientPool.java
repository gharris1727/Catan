package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.network.ControlEvent;
import com.gregswebserver.catan.common.network.Identity;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Greg on 12/29/2014.
 * Pool of clients
 */
public class ClientPool extends EventPayload implements EventConsumer<ControlEvent>, Iterable<ServerClient> {

    private final HashMap<Identity, ServerClient> clients; //Maps identities to the server clients.
    private final HashMap<Identity, Lobby> lobbies; //Maps Users to their lobbies.

    public ClientPool() {
        clients = new HashMap<>();
        lobbies = new HashMap<>();
    }

    public Iterator<ServerClient> iterator() {
        return clients.values().iterator();
    }

    public boolean test(ControlEvent event) {
        Identity origin = event.getOrigin();
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
        Identity origin = event.getOrigin();
        ServerClient client;
        Identity identity;
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
                identity = (Identity) event.getPayload();
                clients.remove(identity);
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
                lobby.setOwner((Identity) event.getPayload());
                break;
            case Lobby_Delete:
                lobby = lobbies.get(event.getOrigin());
                for (Identity clientID : lobby)
                    lobbies.remove(clientID);
                break;
            case Lobby_Join:
                identity = (Identity) event.getPayload();
                lobby = lobbies.get(identity);
                lobby.add(origin);
                lobbies.put(origin, lobby);
                break;
            case Lobby_Leave:
                lobby = lobbies.remove(event.getOrigin());
                lobby.remove(event.getOrigin());
                break;
        }
    }

    public int getConnectionID(Identity identity) {
        if (clients.containsKey(identity)) {
            ServerClient client = clients.get(identity);
            return client.getUniqueID();
        }
        return 0;
    }

    public Lobby getLobby(Identity identity) {
        return lobbies.get(identity);
    }

    public String toString() {
        return "ClientPool";
    }
}
