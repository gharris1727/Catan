package com.gregswebserver.catan.common.game.gameplay;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.gameplay.generator.RandomBoardGenerator;
import com.gregswebserver.catan.common.resources.ResourceLoadException;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType implements Comparable<GameType> {

    private String name;
    private String author;
    private Dimension size;
    private HashSet<Integer> players;
    private HashSet<Coordinate> tradingPosts;
    private HashSet<Coordinate> resourceTiles;

    public GameType(String path) {
        players = new HashSet<>();
        tradingPosts = new HashSet<>();
        resourceTiles = new HashSet<>();
        //TODO: rework game loading.
        /*
        Scanner s = null;
        try {
            s = new Scanner(new FileInputStream(path));
            int lineNumber = 0;
            while (s.hasNext()) {
                String[] line = s.nextLine().split("");
                lineNumber++;
                if (line.length < 2) //Need a minimum of two things to parse, the tag and the data.
                    continue;
                switch (line[0]) {
                    case "#": //Comment tag
                        break;
                    case "N": //Name tag
                        name = line[1];
                        for (int i = 2; i < line.length; i++)
                            name += " " + line[i];
                        break;
                    case "A": //Author tag
                        author = line[1];
                        for (int i = 2; i < line.length; i++)
                            author += " " + line[i];
                        break;
                    case "P": //Player tag
                        int n = Integer.parseInt(line[1]);
                        players.add(n);
                        break;
                    case "S": //Size tag
                        if (line.length < 3)
                            throw new ResourceLoadException("Size tag requires a minimum of two arguments.");
                        int w = Integer.parseInt(line[1]);
                        int h = Integer.parseInt(line[2]);
                        size = new Dimension(w, h);
                        break;
                    case "T": //Trade tag
                        if (line.length < 3)
                            throw new ResourceLoadException("Trade tag requires a minimum of two arguments.");
                        int tx = Integer.parseInt(line[1]);
                        int ty = Integer.parseInt(line[2]);
                        tradingPosts.add(new Coordinate(tx, ty));
                        break;
                    case "R": //Resource tag
                        if (line.length < 3)
                            throw new ResourceLoadException("Resource tag requires a minimum of two arguments.");
                        int rx = Integer.parseInt(line[1]);
                        int ry = Integer.parseInt(line[2]);
                        resourceTiles.add(new Coordinate(rx, ry));
                        break;
                    default: //Unrecognized tag
                        throw new ResourceLoadException("Unknown line tag on line " + lineNumber + ".");
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new ResourceLoadException(e);
        } finally {
            if (s != null)
                s.close();
        }
        */
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
