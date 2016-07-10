package catan.common.game.board.paths;

import catan.common.game.board.TeamOwned;
import catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of all object placed on the edges of hexagons, connecting vertexes.
 */
public abstract class Path extends TeamOwned {

    protected Path(TeamColor owner) {
        super(owner);
    }

}
