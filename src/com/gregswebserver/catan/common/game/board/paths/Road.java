package com.gregswebserver.catan.common.game.board.paths;

import com.gregswebserver.catan.common.game.player.Team;

/**
 * Created by Greg on 8/8/2014.
 * Subclass of Path, rests on the edges of hexagons.
 */
public class Road extends Path {

    public Road(Team owner) {
        super(owner);
    }

    public String toString() {
        return "Road: " + getTeam();
    }

}
