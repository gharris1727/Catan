package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

import java.util.HashSet;

/**
 * Created by Greg on 8/10/2014.
 * Generic generator class that chooses the placement of hexagons on the board.
 */
public interface BoardGenerator {

    public abstract void run(GameBoard board, HashSet<Coordinate> tilePositions, HashSet<Coordinate> tradingPosts);
}
