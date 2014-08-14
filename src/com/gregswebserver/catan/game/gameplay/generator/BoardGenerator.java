package com.gregswebserver.catan.game.gameplay.generator;

import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Tile;

import java.util.HashSet;

/**
 * Created by Greg on 8/10/2014.
 * Generic generator class that chooses the placement of hexagons on the board.
 */
public interface BoardGenerator {

    public abstract void run(HexagonalArray<Tile, Path, Building> hexArray, HashSet<Coordinate> tilePositions, HashSet<Coordinate> tradingPosts);
}
