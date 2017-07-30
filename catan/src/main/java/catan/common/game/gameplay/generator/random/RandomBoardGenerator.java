package catan.common.game.gameplay.generator.random;

import catan.common.game.board.GameBoard;
import catan.common.game.board.Terrain;
import catan.common.game.board.hexarray.CoordTransforms;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.hexarray.HexagonalArray;
import catan.common.game.board.paths.EmptyPath;
import catan.common.game.board.tiles.ResourceTile;
import catan.common.game.board.towns.EmptyTown;
import catan.common.game.gameplay.generator.BoardGenerator;
import catan.common.game.gameplay.layout.BoardLayout;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;

import java.awt.*;
import java.util.*;

/**
 * Created by Greg on 8/10/2014.
 * Tile generator that randomly chooses terrain, dice rolls, and placement.
 */
public class RandomBoardGenerator implements BoardGenerator {

    @Override
    public GameBoard generate(BoardLayout layout, long seed) {
        Random random = new Random(seed);

        Dimension size = layout.getSize();
        HexagonalArray hexArray = new HexagonalArray(size.width, size.height);

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
        Iterator<Coordinate> resourceTiles = layout.getTiles();
        Map<DiceRoll, Set<Coordinate>> diceRolls = new EnumMap<>(DiceRoll.class);
        Coordinate robberLocation = null;
        while (resourceTiles.hasNext()) {
            Coordinate c = resourceTiles.next();
            validVertices.addAll(CoordTransforms.getAdjacentVerticesFromSpace(c).values());
            validEdges.addAll(CoordTransforms.getAdjacentEdgesFromSpace(c).values());
            Terrain t = terrain.next();
            if (t == Terrain.Desert) {
                setResourceTile(hexArray, diceRolls, c,  new ResourceTile(Terrain.Desert, DiceRoll.Seven));
                robberLocation = c;
            } else {
                setResourceTile(hexArray, diceRolls, c, new ResourceTile(t, tokens.next()));
            }
        }

        setRobber(hexArray, robberLocation);

        //Place all of the empty features that fill the rest of the valid map.
        for (Coordinate c : validVertices) {
            hexArray.setTown(c, new EmptyTown());
        }
        for (Coordinate c : validEdges) {
            hexArray.setPath(c, new EmptyPath());
        }

        generateBeachTiles(hexArray);

        //Place trading posts.
        TradeGenerator trades = new TradeGenerator(layout.getPortCount());
        Iterator<Coordinate> tradingPorts = layout.getPorts();
        Set<Coordinate> tradingPosts = new HashSet<>();
        trades.randomize(random);
        for (TradingPostType trade : trades)
            setTradingPost(hexArray, tradingPosts, tradingPorts.next(), trade);

        return new GameBoard(size, hexArray, diceRolls, tradingPosts, robberLocation);
    }

    @Override
    public String toString() {
        return "RandomBoardGenerator";
    }

    public boolean equals(Object o) {
        return (this == o) || ((o != null) && (this.getClass() == o.getClass()));
    }

    public int hashCode() {
        return 0;
    }
}
