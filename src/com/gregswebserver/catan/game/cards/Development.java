package com.gregswebserver.catan.game.cards;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different Development cards.
 */
public enum Development {

    Knight("Knight", 1);

    private String name;
    private int vp;

    Development(String name, int vp) {
        this.name = name;
        this.vp = vp;
    }
}
