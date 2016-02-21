package com.gregswebserver.catan.common.game.gameplay.enums;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum GameResource {

    Brick("Clay"),
    Lumber("Logs"),
    Wool("Sheep"),
    Ore("Rocks"),
    Grain("Wheat");

    public final String name;

    GameResource(String name) {
        this.name = name;
    }
}
