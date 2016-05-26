package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 3/20/16.
 *
 */
public class TeamColors {

    private final Map<TeamColor, int[]> swaps;

    public TeamColors(ConfigSource source) {
        this.swaps = new EnumMap<>(TeamColor.class);
        for (TeamColor teamColor : TeamColor.values()) {
            int[] colorSwaps = new int[]{
                    source.getHexColor("none.primary"), source.getHexColor(teamColor.name + ".primary"),
                    source.getHexColor("none.highlight"), source.getHexColor(teamColor.name + ".primary"),
                    source.getHexColor("none.shadow"), source.getHexColor(teamColor.name + ".shadow"),
            };
            swaps.put(teamColor, colorSwaps);
        }
    }

    public int[] getSwaps(TeamColor teamColor) {
        return swaps.get(teamColor);
    }
}
