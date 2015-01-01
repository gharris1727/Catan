package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 12/29/2014.
 * A set of settings that are configurable on a per-lobby
 */
public class LobbyConfig extends EventPayload {

    private Identity owner;
    private String lobbyName;
    private String mapGenerator;
    private int maxPlayers;

    public LobbyConfig(Identity owner) {
        this.owner = owner;
        lobbyName = owner.username + "'s Game";
        this.mapGenerator = "Default";
        this.maxPlayers = 4;
    }

    public LobbyConfig(Identity owner, String lobbyName, String mapGenerator, int maxPlayers) {
        this.owner = owner;
        this.lobbyName = lobbyName;
        this.mapGenerator = mapGenerator;
        this.maxPlayers = maxPlayers;
    }

    public Identity getOwner() {
        return owner;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getMapGenerator() {
        return mapGenerator;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
