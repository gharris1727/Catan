package com.gregswebserver.catan.common.game.teams;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Greg on 8/9/2014.
 * Enum pertaining to the teamColor allegiance of each player.
 */
public enum TeamColor {

    None, White, Red, Orange, Blue, Green, Brown;

    public static Set<TeamColor> getTeamSet() {
        return EnumSet.range(White, values()[values().length-1]);
    }

}
