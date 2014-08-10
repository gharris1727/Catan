package com.gregswebserver.catan.game.board.buildings;

/**
 * Created by Greg on 8/9/2014.
 * Ocean building object to prevent players from building off the map.
 */
public class OceanBuilding extends Building {


    public OceanBuilding() {
        super(null);
    }

    public int getResourceNumber() {
        return 0;
    }
}
