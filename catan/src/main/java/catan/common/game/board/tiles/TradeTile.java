package catan.common.game.board.tiles;

import catan.common.game.board.hexarray.CoordTransforms;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.util.Direction;

import java.util.Arrays;

import static catan.common.util.Direction.*;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has a trading post on it, also stores the coordinates of the two vertices that can trade with it.
 */
public class TradeTile extends BeachTile {

    private final TradingPostType tradingPostType;
    private final Direction[] directions;

    public TradeTile(Direction direction, int sides, TradingPostType tradingPostType) {
        super(direction, sides);
        this.tradingPostType = tradingPostType;
        switch (direction) {
            case up:
                directions = new Direction[]{upright, upleft};
                break;
            case down:
                directions = new Direction[]{downright, downleft};
                break;
            case left:
                directions = new Direction[]{downleft, left};
                break;
            case right:
                directions = new Direction[]{upright, right};
                break;
            case upleft:
                directions = new Direction[]{upleft, left};
                break;
            case downleft:
                directions = new Direction[]{downleft, left};
                break;
            case upright:
                directions = new Direction[]{upright, right};
                break;
            case downright:
                directions = new Direction[]{downright, right};
                break;
            default:
                //This is the error condition.
                directions = new Direction[]{left, right};
                break;
        }
    }

    public TradingPostType getTradingPostType() {
        return tradingPostType;
    }

    public Direction[] getTradingPostDirections() {
        return directions;
    }

    public Coordinate[] getTradingPostCoordinates() {
        return Arrays.stream(directions)
                .map(direction -> CoordTransforms.getVertexCoordinateFromSpace(getPosition(), direction))
                .toArray(Coordinate[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TradeTile)) return false;
        if (!super.equals(o)) return false;

        TradeTile tradeTile = (TradeTile) o;

        return tradingPostType == tradeTile.tradingPostType;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + tradingPostType.hashCode();
        return result;
    }

    public String toString() {
        return "TradeTile n/" + getSides() + " d/" + getDirection();
    }
}
