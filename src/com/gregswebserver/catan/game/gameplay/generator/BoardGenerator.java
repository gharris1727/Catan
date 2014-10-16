package com.gregswebserver.catan.game.gameplay.generator;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;

import java.util.HashSet;

/**
 * Created by Greg on 8/10/2014.
 * Generic generator class that chooses the placement of hexagons on the board.
 */
public interface BoardGenerator {

    public abstract void run(GameBoard board, HashSet<Coordinate> tilePositions, HashSet<Coordinate> tradingPosts);
}
