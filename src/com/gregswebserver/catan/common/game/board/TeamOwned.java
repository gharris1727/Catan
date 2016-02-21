package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;

/**
 * Created by Greg on 8/8/2014.
 * Generic for a player owned object.
 */
public abstract class TeamOwned extends BoardObject {

    private final Team owner;

    protected TeamOwned(Team owner) {
        this.owner = owner;
    }

    public Team getTeam() {
        return owner;
    }

}
