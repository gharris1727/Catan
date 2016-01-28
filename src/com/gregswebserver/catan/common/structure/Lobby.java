package com.gregswebserver.catan.common.structure;

import com.gregswebserver.catan.common.crypto.Username;

import java.util.HashSet;

/**
 * Created by Greg on 8/22/2014.
 * A serializable set of lobbies that can be sent over the network.
 */
public class Lobby extends HashSet<Username> {

    private LobbyConfig config;

    public Lobby(LobbyConfig config) {
        setConfig(config);
    }

    public LobbyConfig getConfig() {
        return config;
    }

    public void setConfig(LobbyConfig config) {
        this.config = config;
    }

    public String toString() {
        return "Lobby " + config;
    }
}
