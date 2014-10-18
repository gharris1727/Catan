package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.network.Identity;
import com.gregswebserver.catan.server.client.ServerClient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/22/2014.
 * A serializable set of lobbies that can be sent over the network.
 */
public class Lobby {

    //Client-side tracking
    private ArrayList<Identity> identities;
    private Identity owner;
    //Server-side tracking
    private HashMap<Identity, ServerClient> clients;
    private GameThread gameThread;
    private GameType gameType;

    public Lobby(ServerClient owner) {
        identities = new ArrayList<>();
        clients = new HashMap<>();
        addClient(owner);
    }

    public void addClient(ServerClient client) {
        clients.put(client.getIdentity(), client);
        client.setLobby(this);
    }

    public String toString() {
        return owner + "'s Lobby";
    }

    public void rebroadcast(ExternalEvent event) {
        for (ServerClient client : clients.values()) {
            if (!client.getIdentity().equals(event.getOrigin()))
                client.getConnection().sendEvent(event);
        }
    }
}
