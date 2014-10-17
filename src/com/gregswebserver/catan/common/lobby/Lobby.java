package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.common.network.ServerConnection;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/22/2014.
 * A serializable set of lobbies that can be sent over the network.
 */
public class Lobby {

    //Client-side tracking
    ArrayList<Identity> identities;
    private Identity owner;
    //Server-side tracking
    private HashMap<Identity, ServerConnection> clients;
    private GameThread gameThread;
    private GameType gameType;

    public Lobby(ServerConnection owner) {
        identities = new ArrayList<>();
        clients = new HashMap<>();
        addClient(owner);
    }

    public void addClient(ServerConnection client) {
        clients.put(client.getIdentity(), client);
        client.setLobby(this);
    }

    public String toString() {
        return owner + "'s Lobby";
    }

    public HashMap<Identity, ServerConnection> getClients() {
        return clients;
    }

    public void broadcastEvent(ExternalEvent event) {
        //Sends an external event to all clients in the lobby, except for the one who sent it.
        for (ServerConnection connection : clients.values()) {
            if (!connection.getIdentity().equals(event.getOrigin())) {
                connection.sendEvent(event);
            }
        }
    }
}
