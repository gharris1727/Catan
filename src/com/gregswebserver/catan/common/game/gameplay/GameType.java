package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.generator.RandomBoardGenerator;
import com.gregswebserver.catan.common.resources.ConfigFile;

import java.awt.*;
import java.util.HashSet;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType implements Comparable<GameType> {

    private final String name;
    private final String author;
    private final Dimension size;
    private final HashSet<Integer> players;
    private final HashSet<Coordinate> tradingPosts;
    private final HashSet<Coordinate> resourceTiles;

    public GameType(String path) {
        players = new HashSet<>();
        tradingPosts = new HashSet<>();
        resourceTiles = new HashSet<>();
        ConfigFile file = new ConfigFile(path,"Base catan game details");
        name = file.get("name");
        author = file.get("author");
        size = file.getDimension("size");
        boolean p= true, t = true, r = true;
        int i = 0;
        do {
            if (p)
                try {
                    players.add(file.getInt("players." + i));
                } catch (Exception e) {
                    p = false;
                }
            if (t)
                try {
                    tradingPosts.add(file.getCoord("trade." + i));
                } catch (Exception e) {
                    t = false;
                }
            if (r)
                try {
                    resourceTiles.add(file.getCoord("resource." + i));
                } catch (Exception e) {
                    r = false;
                }
            i++;
        } while (p || t || r);
    }

    public String getName() {
        return name;
    }


    public GameBoard generate() {
        GameBoard board = new GameBoard(size);
        //TODO: add ability to change board generators once there are more than one.
        new RandomBoardGenerator().run(board, resourceTiles, tradingPosts);
        return board;
    }

    public String toString() {
        return "GameType: " + name;
    }

    @Override
    public int compareTo(GameType gameType) {
        return 0; //TODO: implement sorting of different game types.
    }
}
