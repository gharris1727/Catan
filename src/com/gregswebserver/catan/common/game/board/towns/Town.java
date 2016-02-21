package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.common.game.board.TeamOwned;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;

/**
 * Created by Greg on 8/8/2014.
 * Superclass of Settlement and City, placed on the vertex of the game board.
 */
public abstract class Town extends TeamOwned {

    Town(Team owner) {
        super(owner);
    }
}
