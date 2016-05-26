package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Building that receives one resource per roll.
 */
public class Settlement extends Town {

    public Settlement(TeamColor owner) {
        super(owner);
    }

    public String toString() {
        return "Settlement: " + getTeam();
    }
}
