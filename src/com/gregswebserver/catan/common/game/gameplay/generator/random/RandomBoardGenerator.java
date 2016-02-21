package com.gregswebserver.catan.common.game.gameplay.generator.random;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.EmptyPath;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.game.gameplay.generator.BoardGenerator;
import com.gregswebserver.catan.common.game.gameplay.layout.BoardLayout;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by Greg on 8/10/2014.
 * Tile generator that randomly chooses terrain, dice rolls, and placement.
 */
public class RandomBoardGenerator implements BoardGenerator {

    public static final RandomBoardGenerator instance = new RandomBoardGenerator();

    private RandomBoardGenerator() {
    }

    @Override
    public GameBoard generate(BoardLayout layout, long seed) {
        Random random = new Random(seed);
        GameBoard board = new GameBoard(layout.getSize());
        Iterator<Coordinate> resourceTiles = layout.getTiles();
        Iterator<Coordinate> tradingPorts = layout.getPorts();
        //Prep all of the helper classes to generate map features.
        TerrainGenerator terrainGenerator = new TerrainGenerator(layout.getTileCount());
        TokenGenerator tokenGenerator = new TokenGenerator(layout.getTileCount() - layout.getDesertCount()); //Don't generate tokens for the desert.
        terrainGenerator.randomize(random);
        tokenGenerator.randomize(random);
        Iterator<Terrain> terrain = terrainGenerator.iterator();
        Iterator<DiceRoll> tokens = tokenGenerator.iterator();

        //Use the valid hexagons to find all valid vertices and edges, as well as adjacent tiles that are beaches.
        Set<Coordinate> validVertices = new HashSet<>();
        Set<Coordinate> validEdges = new HashSet<>();

        //Generate and place playable hexagons
        while (resourceTiles.hasNext()) {
            Coordinate c = resourceTiles.next();
            validVertices.addAll(board.getAdjacentVertices(c));
            validEdges.addAll(board.getAdjacentEdges(c));
            Terrain t = terrain.next();
            if (t.equals(Terrain.Desert)) {
                setResourceTile(board, c, new ResourceTile(t, DiceRoll.Seven));
                board.moveRobber(c);
            } else {
                setResourceTile(board, c, new ResourceTile(t, tokens.next()));
            }
        }

        //Place all of the empty features that fill the rest of the valid map.
        for (Coordinate c : validVertices) {
            board.setBuilding(c, new EmptyTown());
        }
        for (Coordinate c : validEdges) {
            board.setPath(c, new EmptyPath());
        }

        generateBeachTiles(board);

        //Place trading posts.
        TradeGenerator trades = new TradeGenerator(layout.getPortCount());
        trades.randomize(random);
        for (TradingPostType trade : trades)
            setTradingPost(board, tradingPorts.next(), trade);

        return board;
    }

    @Override
    public String toString() {
        return "RandomBoardGenerator";
    }
}
