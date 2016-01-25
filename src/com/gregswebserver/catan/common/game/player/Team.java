package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.DiagonalMask;
import com.gregswebserver.catan.client.graphics.masks.FlippedMask;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.resources.GraphicSourceInfo;

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

    private final GraphicSet graphics;

    Team(String teamConfigKey) {
        GraphicSourceInfo source = new GraphicSourceInfo(Client.staticConfig.get("catan.graphics.teams.paths." + teamConfigKey));

        RenderMask horizontal = new RectangularMask(Client.staticConfig.getDimension("catan.graphics.teams.masks.horizontal.size"));
        RenderMask diagonalUp = new DiagonalMask(Client.staticConfig.getDimension("catan.graphics.teams.masks.diagonal.size"));
        RenderMask diagonalDown = new FlippedMask(diagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask settlement = new RectangularMask(Client.staticConfig.getDimension("catan.graphics.teams.masks.settlement.size"));
        RenderMask city =new RectangularMask(Client.staticConfig.getDimension("catan.graphics.teams.masks.city.size"));
        RenderMask robber = new RectangularMask(Client.staticConfig.getDimension("catan.graphics.teams.masks.robber.size"));
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
        return graphics.getGraphic(6);
    }

}
