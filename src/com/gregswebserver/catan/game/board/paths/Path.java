package com.gregswebserver.catan.game.board.paths;

import com.gregswebserver.catan.game.player.Player;
import com.gregswebserver.catan.game.player.PlayerOwned;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of all object placed on the edges of tiles, connecting vertexes.
 */
public abstract class Path extends PlayerOwned {

    public Path(Player owner) {
        super(owner);
    }
}
