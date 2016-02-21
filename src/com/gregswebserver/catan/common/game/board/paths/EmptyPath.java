package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.common.game.gameplay.enums.Team;

/**
 * Created by Greg on 8/9/2014.
 * Ocean path object to prevent players from building off the map.
 */
public class EmptyPath extends Path {

    public EmptyPath() {
        super(Team.None);
    }

    public String toString() {
        return "Empty Path";
    }
}
