package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.IllegalDirectionException;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.util.Direction;

import java.util.HashSet;

import static com.gregswebserver.catan.common.util.Direction.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has a trading post on it, also stores the coordinates of the two vertices that can trade with it.
 */
public class TradeTile extends BeachTile {

    private final TradingPostType tradingPostType;

    public TradeTile(Direction direction, int sides, TradingPostType tradingPostType) {
        super(direction, sides);
        this.tradingPostType = tradingPostType;
    }

    public TradingPostType getTradingPostType() {
        return tradingPostType;
    }

    public Direction[] getTradingPostDirections() {
        //TODO: generate the trading post directions before this is created.
        //this is fine for 1-sided beach, but we need to deterministically find 2-sided.
        switch (getDirection()) {
            case up:
                return new Direction[]{upright, upleft};
            case down:
                return new Direction[]{downright, downleft};
            case left:
                return new Direction[]{downleft, left};
            case right:
                return new Direction[]{upright, right};
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

    public String toString() {
        return "TradeTile n/" + getSides() + " d/" + getDirection();
    }
}
