package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventPayload;

/**
 * Created by Greg on 12/29/2014.
 * A set of settings that are configurable on a per-lobby
 */
public class LobbyConfig extends EventPayload {

    private final String lobbyName;
    private final String layoutName;
    private final String generatorName;
    private final String rulesetName;
    private final int maxPlayers;

    public LobbyConfig(String lobbyName, String layoutName, String generatorName, String rulesetName, int maxPlayers) {
        this.lobbyName = lobbyName;
        this.layoutName = layoutName;
        this.generatorName = generatorName;
        this.rulesetName = rulesetName;
        this.maxPlayers = maxPlayers;
    }

    public LobbyConfig(Username username) {
        lobbyName = username.username + "'s Game";
        layoutName = "base";
        generatorName = "";
        rulesetName = "default";
        maxPlayers = 4;
    }

    public String getLobbyName() {
        return lobbyName;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public String getRulesetName() {
        return rulesetName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    @Override
    public String toString() {
        return "LobbyConfig(" + lobbyName + ")";
    }
}
