package catan.common.game.board.paths;

import catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Path, rests on the edges of hexagons.
 */
public class Road extends Path {

    public Road(TeamColor owner) {
        super(owner);
    }

    public String toString() {
        return "Road: " + getTeam();
    }

}
