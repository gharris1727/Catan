package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.resources.GraphicSet;

/**
 * Created by Greg on 8/9/2014.
 * Enum pertaining to the team allegiance of each player.
 */
public enum Team {

    None(GraphicSet.TeamEmpty),
    Red(GraphicSet.TeamRed),
    Orange(GraphicSet.TeamOrange),
    Blue(GraphicSet.TeamBlue),
    White(GraphicSet.TeamWhite);

    private GraphicSet graphics;

    Team(GraphicSet graphics) {
        this.graphics = graphics;
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
