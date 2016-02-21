package com.gregswebserver.catan.common.game.gameplay.enums;

/**
 * Created by Greg on 8/9/2014.
 * Enum storing the different Development cards.
 */
public enum DevelopmentCard {

    Knight("Knight", 1),
    VictoryPoint("Victory Point", 1),
    Monopoly("Monopoly", 0),
    YearOfPlenty("Year of Plenty", 0),
    RoadBuilding("Road Building", 0);

    public final String name;
    public final int vp;

    DevelopmentCard(String name, int vp) {
        this.name = name;
        this.vp = vp;
    }

}
