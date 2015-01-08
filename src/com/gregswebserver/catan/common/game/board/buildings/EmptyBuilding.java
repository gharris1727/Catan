package com.gregswebserver.catan.common.game.board.buildings;

/**
 * Created by Greg on 8/9/2014.
 * Ocean building object to prevent players from building off the map.
 */
public class EmptyBuilding extends Building {

    public EmptyBuilding() {
        super(null);
    }

    public int getResourceNumber() {
        return 0;
    }

    public String toString() {
        return "Empty Building";
    }
}
