package com.gregswebserver.catan.game.board;

import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.DiceRoll;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard {

    public HexagonalArray<Tile, Path, Building> hexArray;
    private HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;

    public GameBoard() {
    }

    public void init(int x, int y) {
        hexArray = new HexagonalArray<>(Tile.class, Path.class, Building.class, x, y);
    }
}
