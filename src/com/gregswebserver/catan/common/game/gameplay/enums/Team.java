package com.gregswebserver.catan.common.game.gameplay.enums;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.*;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.GraphicSourceInfo;

import java.util.EnumSet;
import java.util.Set;

/**
 * Created by Greg on 8/9/2014.
 * Enum pertaining to the team allegiance of each player.
 */
public enum Team {

    None("empty"),
    White("white"),
    Red("red"),
    Orange("orange"),
    Blue("blue");
    //TODO: add more teams to support 6 distinct teams at least.

    public static Set<Team> getTeamSet() {
        return EnumSet.range(White, values()[values().length-1]);
    }

    private final GraphicSet graphics;

    Team(String teamConfigKey) {
        GraphicSourceInfo source = new GraphicSourceInfo(Client.graphicsConfig.get("teams.paths." + teamConfigKey));

        RenderMask horizontal = new RectangularMask(Client.graphicsConfig.getDimension("teams.masks.horizontal.size"));
        RenderMask diagonalUp = new AngleRectangularMask(Client.graphicsConfig.getDimension("teams.masks.diagonal.size"),
                Client.graphicsConfig.getDimension("teams.masks.diagonal.offset"));
        RenderMask diagonalDown = new FlippedMask(diagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask settlement = new RoundedMask(Client.graphicsConfig.getDimension("teams.masks.settlement.size"));
        RenderMask city =new RoundedMask(Client.graphicsConfig.getDimension("teams.masks.city.size"));
        RenderMask robber = new RectangularMask(Client.graphicsConfig.getDimension("teams.masks.robber.size"));
        RenderMask[] masks = new RenderMask[]{horizontal, diagonalUp, diagonalDown, settlement, city, robber};
        graphics = new GraphicSet(source, masks);
    }

    public Graphic getRoadGraphic(int orientation) {
        return graphics.getGraphic(orientation);
    }

    public Graphic getSettlementGraphic() {
        return graphics.getGraphic(3);
    }

    public Graphic getCityGraphic() {
        return graphics.getGraphic(4);
    }

    public Graphic getRobberGraphic() {
        return graphics.getGraphic(5);
    }
}
