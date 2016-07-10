package catan.common.game.board.towns;

import catan.common.game.board.TeamOwned;
import catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of Settlement and City, placed on the vertex of the game board.
 */
public abstract class Town extends TeamOwned {

    Town(TeamColor owner) {
        super(owner);
    }
}
