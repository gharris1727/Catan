package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.game.gameplay.generator.RandomBoardGenerator;

import java.util.HashSet;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType {

    public static final GameType BASE_GAME = new GameType("Settlers of Catan Base");

    private String name;

    private HashSet<Coordinate> resourceTiles;
    private HashSet<Coordinate> tradingPosts;
    private int sizeX, sizeY;
    private BoardGenerator boardGenerator;
    private int players;

    public GameType(String name) {
        this.name = name;
    }

    public static void init() {
        BASE_GAME.size(7, 7);
        BASE_GAME.post(0, 3);
        BASE_GAME.post(1, 1).tile(1, 3).post(1, 5);
        BASE_GAME.tile(2, 1).tile(2, 2).tile(2, 3).tile(2, 4).tile(2, 5);
        BASE_GAME.post(3, 0).tile(3, 1).tile(3, 2).tile(3, 3).tile(3, 4).tile(3, 5).post(3, 6);
        BASE_GAME.tile(4, 1).tile(4, 2).tile(4, 3).tile(4, 4).tile(4, 5);
        BASE_GAME.post(5, 0).tile(5, 2).tile(5, 3).tile(5, 4).post(5, 5);
        BASE_GAME.post(6, 2).post(6, 4);
        BASE_GAME.gen(new RandomBoardGenerator());
    }

    //Add a resource tile.
    public GameType tile(int x, int y) {
        resourceTiles.add(new Coordinate(x, y));
        return this;
    }

    //Add a trading post.
    public GameType post(int x, int y) {
        tradingPosts.add(new Coordinate(x, y));
        return this;
    }

    //Set the board size.
    public GameType size(int x, int y) {
        sizeX = x;
        sizeY = y;
        return this;
    }

    //Set the number of players.
    public GameType players(int n) {
        players = n;
        return this;
    }

    //Set the generator logic.
    public GameType gen(BoardGenerator g) {
        boardGenerator = g;
        return this;
    }

    public void LoadSettingsTo(GameBoard board) {
        board.init(sizeX, sizeY, players);
        boardGenerator.run(board.hexArray, resourceTiles, tradingPosts);
    }
}
