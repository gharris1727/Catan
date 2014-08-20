package com.gregswebserver.catan.game.player;

import com.gregswebserver.catan.game.board.BoardObject;

/**
 * Created by Greg on 8/8/2014.
 * Generic for a player owned object.
 */
public abstract class PlayerOwned extends BoardObject {

    private Player owner;

    protected PlayerOwned(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

}
