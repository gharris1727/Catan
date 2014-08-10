package com.gregswebserver.catan.game.cards;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum Resource {

    Brick("Clay"),
    Lumber("Logs"),
    Wool("Sheep"),
    Grain("Wheat"),
    Ore("Rocks");

    private String name;

    Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
