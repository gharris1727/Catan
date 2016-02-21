package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.util.Direction;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Greg on 8/10/2014.
 * Generic generator class that chooses the placement of hexagons on the board.
 */
public interface BoardGenerator extends Serializable {

    GameBoard generate(BoardLayout layout, long seed);

    default void setResourceTile(GameBoard board, Coordinate c, ResourceTile tile) {
        board.setTile(c, tile);
        board.setDiceRollCoordinate(tile.getDiceRoll(), c);
    }

    //This generator call assumes that only resource tiles have been generated.
    default void generateBeachTiles(GameBoard board) {
        for (Coordinate c : board.getBeachTiles(board.getTileMap().keySet())) {
            Set<Direction> found = board.getDirectionsOfAdjacentTiles(c);
            board.setTile(c, new BeachTile(Direction.getAverage(found), found.size()));
        }
    }

    //This generator call assumes beaches have already been generated.
    default void setTradingPost(GameBoard board, Coordinate c, TradingPostType tradeType) {
        BeachTile beach = (BeachTile) board.getTile(c);
        TradeTile tradeTile = new TradeTile(beach.getDirection(), beach.getSides(), tradeType);
        board.setTile(c, tradeTile);
        board.setTradingPostCoordinate(c, tradeType);
    }
}
