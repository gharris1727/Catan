package com.gregswebserver.catan.game.board.buildings;

import com.gregswebserver.catan.game.player.Player;
import com.gregswebserver.catan.game.player.PlayerOwned;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of Settlement and City, placed on the vertex of the game board.
 */
public abstract class Building extends PlayerOwned {

    public Building(Player owner) {
        super(owner);
    }

    public abstract int getResourceNumber();
}
