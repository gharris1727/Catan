package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.resources.GraphicInfo;
import com.gregswebserver.catan.client.resources.RenderMasks;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.IllegalDirectionException;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPost;
import com.gregswebserver.catan.common.resources.ResourceLoader;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.util.HashSet;

import static com.gregswebserver.catan.client.resources.GraphicInfo.*;
import static com.gregswebserver.catan.common.util.Direction.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has a trading post on it, also stores the coordinates of the two vertices that can trade with it.
 */
public class TradeTile extends BeachTile {

    private static final GraphicInfo[] tradeGraphics = new GraphicInfo[]{
            null, null, null, TradeLeft, TradeRight, TradeUpLeft, TradeDownLeft, TradeUpRight, TradeDownRight
    };
    private static final Point[] position = new Point[]{
            new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point()
    };

    private TradingPost tradingPost;
    private Graphic graphic;

    public TradeTile(Direction direction, int sides, TradingPost tradingPost) {
        super(direction, sides);
        this.tradingPost = tradingPost;
    }

    public TradingPost getTradingPost() {
        return tradingPost;
    }

    private Direction[] getTradingPostDirections() {
        switch (getDirection()) {
            case up:
                return new Direction[]{upright, upleft};
            case down:
                return new Direction[]{downright, downleft};
            case upleft:
                return new Direction[]{upleft, left};
            case downleft:
                return new Direction[]{downleft, left};
            case upright:
                return new Direction[]{upright, right};
            case downright:
                return new Direction[]{downright, right};
            default:
                return new Direction[]{};
        }
    }

    public HashSet<Coordinate> getTradingPostCoordinates() {
        HashSet<Coordinate> out = new HashSet<>(3);
        for (Direction d : getTradingPostDirections()) {
            try {
                out.add(getHexArray().getVertexCoordinateFromSpace(getPosition(), d));
            } catch (IllegalDirectionException e) {
                //Shouldn't happen.
            }
        }
        return out;
    }

    public Graphic getGraphic() {
        if (graphic == null) {
            graphic = new Graphic(RenderMasks.TileMask.getMask());
            Graphic b = super.getGraphic();
            b.renderTo(graphic, new Point(), 0);
            for (Direction d : getTradingPostDirections()) {
                Graphic t = ResourceLoader.getGraphic(tradeGraphics[d.ordinal()]);
                t.renderTo(graphic, position[d.ordinal()], 0);
            }
        }
        return graphic;
    }

    public String toString() {
        return "TradeTile n/" + getSides() + " d/" + getDirection();
    }
}
