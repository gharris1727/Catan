package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.DiagonalMask;
import com.gregswebserver.catan.client.graphics.masks.FlippedMask;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.client.resources.GraphicSourceInfo;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.IllegalDirectionException;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPost;
import com.gregswebserver.catan.common.util.Direction;
import com.sun.istack.internal.NotNull;

import java.awt.*;
import java.util.HashSet;

import static com.gregswebserver.catan.common.util.Direction.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has a trading post on it, also stores the coordinates of the two vertices that can trade with it.
 */
public class TradeTile extends BeachTile {

    private static final Point[] position = new Point[]{
            new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point(), new Point()
    };

    private static final GraphicSet graphics;

    static {
        GraphicSourceInfo source = new GraphicSourceInfo(Main.staticConfig.get("catan.graphics.trade.bridge.path"));
        RenderMask horizontal = new RectangularMask(Main.staticConfig.getDimension("catan.graphics.trade.bridge.horizontal.size"));
        RenderMask diagonalUp = new DiagonalMask(Main.staticConfig.getDimension("catan.graphics.trade.bridge.diagonal.size"));
        RenderMask diagonalDown = new FlippedMask(diagonalUp, FlippedMask.Direction.VERTICAL);
        RenderMask[] masks = new RenderMask[]{null, null, null, horizontal,
                horizontal, diagonalDown, diagonalDown, diagonalUp, diagonalUp};
        graphics = new GraphicSet(source,masks);
    }

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

    @NotNull
    @Override
    public Graphic getGraphic() {
        if (graphic == null) {
            graphic = new Graphic(tileMask);
            Graphic b = super.getGraphic();
            b.renderTo(graphic, new Point(), 0);
            for (Direction d : getTradingPostDirections()) {
                graphics.getGraphic(d.ordinal()).renderTo(graphic, position[d.ordinal()], 0);
            }
        }
        return graphic;
    }

    public String toString() {
        return "TradeTile n/" + getSides() + " d/" + getDirection();
    }
}
