package catan.common.game.gameplay.generator;

import catan.common.game.board.GameBoard;
import catan.common.game.board.hexarray.CoordTransforms;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.hexarray.HexagonalArray;
import catan.common.game.board.tiles.BeachTile;
import catan.common.game.board.tiles.ResourceTile;
import catan.common.game.board.tiles.Tile;
import catan.common.game.board.tiles.TradeTile;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;
import catan.common.util.Direction;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Created by Greg on 8/10/2014.
 * Generic generator class that chooses the placement of hexagons on the board.
 */
@FunctionalInterface
public interface BoardGenerator extends Serializable {

    GameBoard generate(BoardLayout layout, long seed);

    default void setResourceTile(HexagonalArray hexArray, Map<DiceRoll, Set<Coordinate>> diceRolls, Coordinate coord, ResourceTile tile) {
        DiceRoll diceRoll = tile.getDiceRoll();
        hexArray.setTile(coord, tile);
        Set<Coordinate> coordinates = diceRolls.computeIfAbsent(diceRoll, k -> new HashSet<>());
        coordinates.add(coord);
    }

    //This generator call assumes that only resource tiles have been generated.
    default void generateBeachTiles(HexagonalArray hexArray) {
        Set<Coordinate> landTiles = hexArray.getSpaceCoordinates();

        Set<Coordinate> beachTiles = new HashSet<>();
        for (Coordinate c1 : landTiles)
            beachTiles.addAll(CoordTransforms.getAdjacentSpacesFromSpace(c1).values());
        beachTiles.removeAll(landTiles);

        for (Coordinate coord : beachTiles) {
            Set<Direction> found = EnumSet.noneOf(Direction.class);
            for (Entry<Direction, Coordinate> e : CoordTransforms.getAdjacentSpacesFromSpace(coord).entrySet()) {
                Tile t = hexArray.getTile(e.getValue());
                if ((t != null) && (t instanceof ResourceTile))
                    found.add(e.getKey());
            }
            hexArray.setTile(coord, new BeachTile(Direction.getAverage(found), found.size()));
        }
    }

    //This generator call assumes beaches have already been generated.
    default void setTradingPost(HexagonalArray hexArray, Set<Coordinate> tradingPosts, Coordinate coord, TradingPostType tradeType) {
        BeachTile beach = (BeachTile) hexArray.getTile(coord);
        hexArray.setTile(coord, new TradeTile(beach.getDirection(), beach.getSides(), tradeType));
        tradingPosts.add(coord);
    }

    default void setRobber(HexagonalArray hexArray, Coordinate robberLocation) {
        ((ResourceTile)  hexArray.getTile(robberLocation)).placeRobber();
    }
}
