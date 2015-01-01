package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.network.Identity;

import java.util.HashSet;

/**
 * Created by Greg on 8/22/2014.
 * A serializable set of lobbies that can be sent over the network.
 */
public class Lobby extends HashSet<Identity> {

    private Identity owner;
    private LobbyConfig config;

    public Lobby(Identity owner) {
        setOwner(owner);
    }

    public Identity getOwner() {
        return owner;
    }

    public void setOwner(Identity owner) {
        this.owner = owner;
        add(owner);
    }

    public LobbyConfig getConfig() {
        return config;
    }

    public void setConfig(LobbyConfig config) {
        this.config = config;
    }

    public String toString() {
        return config.getOwner().username + "'s Lobby: " + config.getLobbyName();
    }
}
