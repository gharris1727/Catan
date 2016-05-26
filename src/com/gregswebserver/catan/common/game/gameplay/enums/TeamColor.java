package com.gregswebserver.catan.common.game.gameplay.enums;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Greg on 8/9/2014.
 * Enum pertaining to the teamColor allegiance of each player.
 */
public enum TeamColor {

    None("none"),
    White("white"),
    Red("red"),
    Orange("orange"),
    Blue("blue"),
    Green("green"),
    Brown("brown");

    public static Set<TeamColor> getTeamSet() {
        return EnumSet.range(White, values()[values().length-1]);
    }

    public final String name;

    TeamColor(String name) {
        this.name = name;
    }
}
