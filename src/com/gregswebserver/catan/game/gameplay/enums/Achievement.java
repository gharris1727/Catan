package com.gregswebserver.catan.game.gameplay.enums;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different achievement cards.
 */
public enum Achievement {

    LongestRoad("Longest Road", 2);

    private String name;
    private int vp;

    Achievement(String name, int vp) {
        this.name = name;
        this.vp = vp;

    }
}
