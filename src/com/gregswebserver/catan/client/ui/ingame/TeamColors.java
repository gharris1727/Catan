package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 3/20/16.
 *
 */
public class TeamColors {

    private final Map<Team, int[]> swaps;

    public TeamColors(ConfigSource source) {
        this.swaps = new EnumMap<>(Team.class);
        for (Team team : Team.values()) {
            int[] colorSwaps = new int[]{
                    source.getHexColor("none.primary"), source.getHexColor(team.name + ".primary"),
                    source.getHexColor("none.highlight"), source.getHexColor(team.name + ".primary"),
                    source.getHexColor("none.shadow"), source.getHexColor(team.name + ".shadow"),
            };
            swaps.put(team, colorSwaps);
        }
    }

    public int[] getSwaps(Team team) {
        return swaps.get(team);
    }
}
