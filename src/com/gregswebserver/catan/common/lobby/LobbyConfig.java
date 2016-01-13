package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.event.EventPayload;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gameplay.GameType;

/**
 * Created by Greg on 12/29/2014.
 * A set of settings that are configurable on a per-lobby
 */
public class LobbyConfig extends EventPayload {

    private String lobbyName;
    private String mapGenerator;
    private int maxPlayers;

    public LobbyConfig(String lobbyName, String mapGenerator, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.mapGenerator = mapGenerator;
        this.maxPlayers = maxPlayers;
    }

    public LobbyConfig(Username username) {
        lobbyName = username.username + "'s Game";
        mapGenerator = "default";
        maxPlayers = 3;
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
