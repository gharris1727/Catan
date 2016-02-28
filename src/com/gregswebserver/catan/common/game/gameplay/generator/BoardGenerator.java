package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;
import com.gregswebserver.catan.common.util.Direction;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Greg on 8/10/2014.
 * Generic generator class that chooses the placement of hexagons on the board.
 */
public interface BoardGenerator extends Serializable {

    GameBoard generate(BoardLayout layout, long seed);

    default void setResourceTile(HexagonalArray hexArray, Map<DiceRoll, List<Coordinate>> diceRolls, Coordinate c, ResourceTile tile) {
        DiceRoll diceRoll = tile.getDiceRoll();
        hexArray.setTile(c, tile);
        List<Coordinate> coordinates = diceRolls.get(diceRoll);
        if (coordinates == null)
            diceRolls.put(diceRoll, coordinates = new ArrayList<>());
        coordinates.add(c);
    }

    //This generator call assumes that only resource tiles have been generated.
    default void generateBeachTiles(HexagonalArray hexArray) {
        Set<Coordinate> landTiles = hexArray.spaces.toMap().keySet();

        Set<Coordinate> beachTiles = new HashSet<>();
        for (Coordinate c1 : landTiles)
            beachTiles.addAll(hexArray.getAdjacentSpacesFromSpace(c1).values());
        beachTiles.removeAll(landTiles);

        for (Coordinate c : beachTiles) {
            Set<Direction> found = EnumSet.noneOf(Direction.class);
            for (Map.Entry<Direction, Coordinate> e : hexArray.getAdjacentSpacesFromSpace(c).entrySet()) {
                Tile t = hexArray.getTile(e.getValue());
                if (t != null && t instanceof ResourceTile)
                    found.add(e.getKey());
            }
            hexArray.setTile(c, new BeachTile(Direction.getAverage(found), found.size()));
        }
    }

    //This generator call assumes beaches have already been generated.
    default void setTradingPost(HexagonalArray hexArray, Map<Coordinate, TradingPostType> tradingPosts, Coordinate c, TradingPostType tradeType) {
        BeachTile beach = (BeachTile) hexArray.getTile(c);
        TradeTile tradeTile = new TradeTile(beach.getDirection(), beach.getSides(), tradeType);
        hexArray.setTile(c, tradeTile);
        tradingPosts.put(c, tradeType);
    }
}
