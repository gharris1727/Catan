package com.gregswebserver.catan.game.gameplay.enums;

import com.gregswebserver.catan.game.gameplay.trade.Tradeable;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum Resource implements Tradeable {

    Brick("Clay"),
    Lumber("Logs"),
    Wool("Sheep"),
    Grain("Wheat"),
    Ore("Rocks");

    private final String name;

    Resource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
