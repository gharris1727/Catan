package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.config.Parser;
import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.gameplay.generator.Generator;
import com.gregswebserver.catan.game.gameplay.generator.RandomGenerator;

import java.io.IOException;
import java.util.HashSet;

/**
 * Created by Greg on 8/9/2014.
 * Static data in order to create a game of Catan.
 */
public class GameType {

    private String name;

    private HashSet<Coordinate> resourceTiles;
    private HashSet<Coordinate> tradeVertices;
    private Coordinate boardSize;
    private Generator generator;
    private int players;

    GameType(String fileName) {
        try {
            Parser parser = new Parser(fileName);
            String data;
            while ((data = parser.readData()) != null) {
                switch (parser.getActiveTag()) {
                    case Comment:
                        //Ignore
                        break;
                    case Tile:
                        //Add a resource tile to the game board.
                        Coordinate tile = new Coordinate(data);
                        resourceTiles.add(tile);
                        break;
                    case Name:
                        //Store the name of the GameType
                        name = data;
                        break;
                    case Size:
                        //Store the size of the game board.
                        Coordinate size = new Coordinate(data);
                        boardSize = size;
                        break;
                    case Trade:
                        Coordinate trade = new Coordinate(data);
                        tradeVertices.add(trade);
                        break;
                    case Generator:
                        switch (data) {
                            case "Random":
                                generator = new RandomGenerator();
                                break;
                            case "Fair":
                                //TODO: add more generators
                                break;
                        }
                        break;
                    case Players:
                        players = Integer.parseInt(data);
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(String fileName) {
        //TODO: save custom game modes to file
    }

    public void LoadSettingsTo(GameBoard board) {
        board.init(boardSize.getX(), boardSize.getY());
        generator.run(board.hexArray, resourceTiles);

    }
}
