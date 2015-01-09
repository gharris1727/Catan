package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.common.game.board.BoardObject;

/**
 * Created by Greg on 8/8/2014.
 * Generic for a player owned object.
 */
public abstract class TeamOwned extends BoardObject {

    private Team owner;

    protected TeamOwned(Team owner) {
        this.owner = owner;
    }

    public Team getTeam() {
        return owner;
    }

}
