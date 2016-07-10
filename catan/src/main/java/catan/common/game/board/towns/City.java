package catan.common.game.board.towns;

import catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of building that gives two resources per roll.
 */
public class City extends Town {

    public City(TeamColor owner) {
        super(owner);
    }

    public String toString() {
        return "City: " + getTeam();
    }
}
