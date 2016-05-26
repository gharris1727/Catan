package com.gregswebserver.catan.common.game.board.towns;

import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/9/2014.
 * Ocean building object to prevent players from building off the map.
 */
public class EmptyTown extends Town {

    public EmptyTown() {
        super(TeamColor.None);
    }

    public String toString() {
        return "Empty Building";
    }
}
