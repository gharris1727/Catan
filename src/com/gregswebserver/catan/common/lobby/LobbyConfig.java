package com.gregswebserver.catan.common.lobby;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventPayload;

/**
 * Created by Greg on 12/29/2014.
 * A set of settings that are configurable on a per-lobby
 */
public class LobbyConfig extends EventPayload {

    private final String lobbyName;
    private final String gameType;
    private final String generator;
    private final int maxPlayers;

    public LobbyConfig(String lobbyName, String gameType, String generator, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.gameType = gameType;
        this.generator = generator;
        this.maxPlayers = maxPlayers;
    }

    public LobbyConfig(Username username) {
        lobbyName = username.username + "'s Game";
        gameType = "default";
        generator = "random";
        maxPlayers = 3;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getGameType() {
        return gameType;
    }

    public String getGenerator() {
        return generator;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
}
