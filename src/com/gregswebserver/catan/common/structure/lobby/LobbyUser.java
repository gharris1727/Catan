package com.gregswebserver.catan.common.structure.lobby;

import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by greg on 2/5/16.
 * A user in a lobby. Stores some information relevant to that user's preferences in game.
 */
public class LobbyUser {

    private TeamColor teamColor;

    public LobbyUser() {
        this.teamColor = TeamColor.None;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(TeamColor teamColor) {
        this.teamColor = teamColor;
    }
}
