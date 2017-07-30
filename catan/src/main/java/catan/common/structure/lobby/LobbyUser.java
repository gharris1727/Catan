package catan.common.structure.lobby;

import catan.common.game.teams.TeamColor;

import java.io.Serializable;

/**
 * Created by greg on 2/5/16.
 * A user in a lobby. Stores some information relevant to that user's preferences in game.
 */
public class LobbyUser implements Serializable {

    private TeamColor teamColor;

    public LobbyUser() {
        teamColor = TeamColor.None;
    }

    public TeamColor getTeamColor() {
        return teamColor;
    }

    public void setTeamColor(TeamColor teamColor) {
        this.teamColor = teamColor;
    }
}
