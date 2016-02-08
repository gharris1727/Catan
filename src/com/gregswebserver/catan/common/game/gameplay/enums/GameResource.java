package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.common.game.gameplay.trade.Tradeable;

/**
 * Created by Greg on 8/9/2014.
 * Enum for storing the different resources.
 */
public enum GameResource implements Tradeable {

    Brick("Clay"),
    Lumber("Logs"),
    Wool("Sheep"),
    Ore("Rocks"),
    Grain("Wheat"),
    Wildcard("Any");

    private final String name;

    GameResource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
