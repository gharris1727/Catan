package com.gregswebserver.catan.common.game.board.tiles;

import com.gregswebserver.catan.common.game.board.hexarray.Direction;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPost;

import java.util.HashSet;

/**
 * Created by Greg on 8/22/2014.
 * A tile that has a trading post on it, also stores the coordinates of the two vertices that can trade with it.
 */
public class TradeTile extends BeachTile {

    private TradingPost tradingPost;
    private HashSet<Direction> directions;

    public TradeTile(int sides, Direction direction, TradingPost tradingPost) {
        super(direction, sides);
        this.tradingPost = tradingPost;
        directions = new HashSet<>();
        switch (direction) {
            case up:
                directions.add(Direction.upright);
                directions.add(Direction.upleft);
                break;
            case down:
                directions.add(Direction.downright);
                directions.add(Direction.downleft);
                break;
            case upleft:
                directions.add(Direction.upleft);
                directions.add(Direction.left);
                break;
            case downleft:
                directions.add(Direction.downleft);
                directions.add(Direction.left);
                break;
            case upright:
                directions.add(Direction.upright);
                directions.add(Direction.right);
                break;
            case downright:
                directions.add(Direction.downright);
                directions.add(Direction.right);
                break;
            default:
                //uh.
        }
    }

    public TradingPost getTradingPost() {
        return tradingPost;
    }

    public HashSet<Direction> getTradingPostDirections() {
        return directions;
    }

    public String toString() {
        return "TradeTile " + tradingPost;
    }
}
