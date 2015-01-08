package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.game.player.PlayerOwned;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of all object placed on the edges of hexagons, connecting vertexes.
 */
public abstract class Path extends PlayerOwned {

    public Path(Player owner) {
        super(owner);
    }

}
