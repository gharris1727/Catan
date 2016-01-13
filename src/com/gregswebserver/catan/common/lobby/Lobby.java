package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.crypto.Username;

import java.util.HashSet;

/**
 * Created by Greg on 8/22/2014.
 * A serializable set of lobbies that can be sent over the network.
 */
public class Lobby extends HashSet<Username> {

    private Username owner;
    private LobbyConfig config;

    public Lobby(Username owner, LobbyConfig config) {
        setOwner(owner);
        setConfig(config);
    }

    public Username getOwner() {
        return owner;
    }

    public void setOwner(Username owner) {
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
        return "Lobby UID: " + owner;
    }
}
