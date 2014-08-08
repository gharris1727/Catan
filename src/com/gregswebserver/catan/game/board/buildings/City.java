package com.gregswebserver.catan.game.board.buildings;

import com.gregswebserver.catan.game.player.Player;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of building that gives two resources per roll.
 */
public class City extends Building {

    public City(Player owner) {
        super(owner);
    }

    public int getResourceNumber() {
        return 2;
    }
}
