package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;

/**
 * Created by greg on 2/5/16.
 * A user in a lobby. Stores some information relevant to that user's preferences in game.
 */
public class LobbyUser {

    private Team team;

    public LobbyUser() {
        this.team = Team.None;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
