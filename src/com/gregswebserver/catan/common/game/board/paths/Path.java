package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.common.game.player.Team;
import com.gregswebserver.catan.common.game.player.TeamOwned;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of all object placed on the edges of hexagons, connecting vertexes.
 */
public abstract class Path extends TeamOwned {

    protected Path(Team owner) {
        super(owner);
    }

}
