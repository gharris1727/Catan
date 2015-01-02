package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPost;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard {

    public final Dimension size;
    public final HexagonalArray<Tile, Path, Building> hexArray;
    private HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;
    private HashMap<Coordinate, TradingPost> tradingPosts;

    public GameBoard(Dimension size) {
        this.size = size;
        hexArray = new HexagonalArray<>(Tile.class, Path.class, Building.class, size.width, size.height);
        diceRollCoordinates = new HashMap<>();
        tradingPosts = new HashMap<>();
    }

    public void setDiceRollCoordinate(DiceRoll diceRoll, Coordinate c) {
        ArrayList<Coordinate> coordinates;
        if (!diceRollCoordinates.containsKey(diceRoll)) {
            coordinates = new ArrayList<>();
            diceRollCoordinates.put(diceRoll, coordinates);
        } else {
            coordinates = diceRollCoordinates.get(diceRoll);
        }
        coordinates.add(c);
    }

    public void setTradingPostCoordinate(Coordinate coordinate, TradingPost tradingPost) {
        tradingPosts.put(coordinate, tradingPost);
    }
}
