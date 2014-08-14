package com.gregswebserver.catan.game.gameplay.enums;

import com.gregswebserver.catan.game.gameplay.trade.Tradeable;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different Development cards.
 */
public enum DevelopmentCard implements Tradeable {

    Knight("Knight", 1),
    Progress("Progress Card", 0),
    VictoryPoint("Victory Point Card", 1);

    private String name;
    private int vp;

    DevelopmentCard(String name, int vp) {
        this.name = name;
        this.vp = vp;
    }
}
