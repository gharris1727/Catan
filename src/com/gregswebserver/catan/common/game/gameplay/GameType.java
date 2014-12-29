package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType implements Serializable {

    private String name;

    private HashSet<Coordinate> resourceTiles;
    private HashSet<Coordinate> tradingPosts;
    private HashSet<Integer> players;
    private int sizeX, sizeY;
    private BoardGenerator boardGenerator;

    public GameType(String name) {
        this.name = name;
        resourceTiles = new HashSet<>();
        tradingPosts = new HashSet<>();
        players = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public GameType tile(int x, int y) {
        resourceTiles.add(new Coordinate(x, y));
        return this;
    }

    public GameType post(int x, int y) {
        tradingPosts.add(new Coordinate(x, y));
        return this;
    }

    public GameType size(int x, int y) {
        sizeX = x;
        sizeY = y;
        return this;
    }

    public GameType players(int n) {
        players.add(n);
        return this;
    }

    public GameType gen(BoardGenerator g) {
        boardGenerator = g;
        return this;
    }

    public void generate(GameBoard board) {
        board.init(sizeX, sizeY);
        boardGenerator.run(board, resourceTiles, tradingPosts);
    }

    public String toString() {
        return "GameType: " + name;
    }
}