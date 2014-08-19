package com.gregswebserver.catan.game.player;

/**
 * Created by Greg on 8/8/2014.
 * Generic for a player owned object.
 */
public abstract class PlayerOwned {

    private Player owner;

    protected PlayerOwned(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

}
