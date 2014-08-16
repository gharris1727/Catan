package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.gameplay.generator.BoardGenerator;

import java.util.HashSet;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType {

    private String name;

    private HashSet<Coordinate> resourceTiles;
    private HashSet<Coordinate> tradingPosts;
    private int sizeX, sizeY;
    private BoardGenerator boardGenerator;
    private int players;

    public GameType(String name) {
        this.name = name;
        resourceTiles = new HashSet<>();
        tradingPosts = new HashSet<>();
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
        players = n;
        return this;
    }

    public GameType gen(BoardGenerator g) {
        boardGenerator = g;
        return this;
    }

    public void LoadSettingsTo(GameBoard board) {
        board.init(sizeX, sizeY, players);
        boardGenerator.run(board.hexArray, resourceTiles, tradingPosts);
    }
}
