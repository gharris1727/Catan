package com.gregswebserver.catan.common.game.player;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import static com.gregswebserver.catan.client.resources.GraphicInfo.*;

/**
 * Created by Greg on 8/9/2014.
 * Enum pertaining to the team allegiance of each player.
 */
public enum Team {

    None, Red, Orange, Blue, White;

    private GraphicInfo[][] graphics = new GraphicInfo[][]{
            {EmptyPathHorizontal, EmptyPathDiagonalUp, EmptyPathDiagonalDown, EmptySettlement, null},
            {RedPathHorizontal, RedPathDiagonalUp, RedPathDiagonalDown, RedSettlement, RedCity},
            {OrangePathHorizontal, OrangePathDiagonalUp, OrangePathDiagonalDown, OrangeSettlement, OrangeCity},
            {BluePathHorizontal, BluePathDiagonalUp, BluePathDiagonalDown, BlueSettlement, BlueCity},
            {WhitePathHorizontal, WhitePathDiagonalUp, WhitePathDiagonalDown, WhiteSettlement, WhiteCity}
    };

    public Graphic getRoadGraphic(int orientation) {
        return ResourceLoader.getGraphic(graphics[ordinal()][orientation]);
    }

    public Graphic getSettlementGraphic() {
        return ResourceLoader.getGraphic(graphics[ordinal()][3]);
    }

    public Graphic getCityGraphic() {
        return ResourceLoader.getGraphic(graphics[ordinal()][4]);
    }

}
