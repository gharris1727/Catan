package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by Greg on 8/9/2014.
 * Ocean path object to prevent players from building off the map.
 */
public class EmptyPath extends Path {

    public EmptyPath() {
        super(TeamColor.None);
    }

    public String toString() {
        return "Empty Path";
    }
}
