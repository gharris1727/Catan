package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.*;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.GraphicSourceInfo;
import com.gregswebserver.catan.common.resources.PropertiesFile;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by greg on 3/20/16.
 *
 */
public class TeamGraphics {

    private static final GraphicSourceInfo teamSource;
    private static final RenderMask[] teamMasks;

    static {
        teamSource = new GraphicSourceInfo(Client.graphicsConfig.get("teams.source"));
        RenderMask horizontal = new RectangularMask(Client.graphicsConfig.getDimension("teams.masks.horizontal.size"));
        RenderMask diagonalUp = new AngleRectangularMask(Client.graphicsConfig.getDimension("teams.masks.diagonal.size"),
                Client.graphicsConfig.getDimension("teams.masks.diagonal.offset"));
        RenderMask diagonalDown = new FlippedMask(diagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask settlement = new RoundedMask(Client.graphicsConfig.getDimension("teams.masks.settlement.size"));
        RenderMask city =new RoundedMask(Client.graphicsConfig.getDimension("teams.masks.city.size"));
        RenderMask robber = new RectangularMask(Client.graphicsConfig.getDimension("teams.masks.robber.size"));
        teamMasks = new RenderMask[]{horizontal, diagonalUp, diagonalDown, settlement, city, robber};
    }

    private final PropertiesFile teamColors;
    private final Map<Team, int[]> swaps;
    private final Map<Team, GraphicSet> teamGraphics;
    private final Map<Team, GraphicSet> eventGraphics;

    public TeamGraphics(PropertiesFile teamColors) {
        this.teamColors = teamColors;
        this.swaps = new EnumMap<>(Team.class);
        this.teamGraphics = new EnumMap<>(Team.class);
        this.eventGraphics = new EnumMap<>(Team.class);
    }

    private void loadColors(Team team) {
        if (!swaps.containsKey(team)) {
            int[] colorSwaps = new int[]{
                    teamColors.getHexColor("none.primary"), teamColors.getHexColor(team.name + ".primary"),
                    teamColors.getHexColor("none.highlight"), teamColors.getHexColor(team.name + ".primary"),
                    teamColors.getHexColor("none.shadow"), teamColors.getHexColor(team.name + ".shadow"),
            };
            swaps.put(team, colorSwaps);
        }
    }

    private GraphicSet getTeamGraphics(Team team) {
        loadColors(team);
        if (!teamGraphics.containsKey(team)) {
            GraphicSet graphics = new GraphicSet(teamSource, teamMasks, swaps.get(team));
            teamGraphics.put(team, graphics);
        }
        return teamGraphics.get(team);
    }

    private GraphicSet getEventGraphics(Team team) {
        loadColors(team);
        if (!eventGraphics.containsKey(team)) {
            GraphicSet graphics = new GraphicSet(Client.graphicsConfig, "events.source", ChevronMask.class, swaps.get(team));
            eventGraphics.put(team, graphics);
        }
        return eventGraphics.get(team);
    }

    public Graphic getRoadGraphic(Team team, int orientation) {
        return getTeamGraphics(team).getGraphic(orientation);
    }

    public Graphic getSettlementGraphic(Team team) {
        return getTeamGraphics(team).getGraphic(3);
    }

    public Graphic getCityGraphic(Team team) {
        return getTeamGraphics(team).getGraphic(4);
    }

    public Graphic getRobberGraphic(Team team) {
        return getTeamGraphics(team).getGraphic(5);
    }

    public Graphic getEventGraphic(Team team, GameEvent event) {
        return getEventGraphics(team).getGraphic(event.getType());
    }
}
