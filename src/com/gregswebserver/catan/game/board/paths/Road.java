package com.gregswebserver.catan.game.board.paths;

import com.gregswebserver.catan.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Path, rests on the edges of tiles.
 */
public class Road extends Path {

    public Road(Player owner) {
        super(owner);
    }
}
