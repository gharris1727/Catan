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
            beachTiles.addAll(CoordTransforms.getAdjacentSpacesFromSpace(c1).values());
        beachTiles.removeAll(landTiles);

        for (Coordinate c : beachTiles) {
            Set<Direction> found = EnumSet.noneOf(Direction.class);
            for (Map.Entry<Direction, Coordinate> e : CoordTransforms.getAdjacentSpacesFromSpace(c).entrySet()) {
                Tile t = hexArray.getTile(e.getValue());
                if (t != null && t instanceof ResourceTile)
                    found.add(e.getKey());
            }
            hexArray.setTile(c, new BeachTile(Direction.getAverage(found), found.size()));
        }
    }

    //This generator call assumes beaches have already been generated.
    default void setTradingPost(HexagonalArray hexArray, List<Coordinate> tradingPosts, Coordinate c, TradingPostType tradeType) {
        BeachTile beach = (BeachTile) hexArray.getTile(c);
        hexArray.setTile(c, new TradeTile(beach.getDirection(), beach.getSides(), tradeType));
        tradingPosts.add(c);
    }

    default void setRobber(HexagonalArray hexArray, Coordinate robberLocation) {
        ((ResourceTile)  hexArray.getTile(robberLocation)).placeRobber();
    }
}
