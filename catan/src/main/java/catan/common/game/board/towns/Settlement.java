package catan.common.game.board.towns;

import catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Building that receives one resource per roll.
 */
public class Settlement extends Town {

    public Settlement(TeamColor owner) {
        super(owner);
    }

    public String toString() {
        return "Settlement: " + getTeam();
    }
}
