package catan.client.ui.game;

import catan.common.config.ConfigSource;
import catan.common.game.teams.TeamColor;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 3/20/16.
 *
 */
public class TeamColorSwaps {

    private final Map<TeamColor, int[]> swaps;

    public TeamColorSwaps(ConfigSource source) {
        this.swaps = new EnumMap<>(TeamColor.class);
        for (TeamColor teamColor : TeamColor.values()) {
            int[] colorSwaps = new int[]{
                    source.getHexColor("None.primary"), source.getHexColor(teamColor + ".primary"),
                    source.getHexColor("None.highlight"), source.getHexColor(teamColor + ".primary"),
                    source.getHexColor("None.shadow"), source.getHexColor(teamColor + ".shadow"),
            };
            swaps.put(teamColor, colorSwaps);
        }
    }

    public int[] getSwaps(TeamColor teamColor) {
        return swaps.get(teamColor);
    }
}
