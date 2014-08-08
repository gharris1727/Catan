package com.gregswebserver.catan.game.board.buildings;

import com.gregswebserver.catan.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Building that receives one resource per roll.
 */
public class Settlement extends Building {

    public Settlement(Player owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 2;
    }
}
