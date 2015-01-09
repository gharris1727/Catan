package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.Direction;
import com.gregswebserver.catan.common.game.board.paths.EmptyPath;
import com.gregswebserver.catan.common.game.board.tiles.BeachTile;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPost;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Greg on 8/10/2014.
 * Tile generator that randomly chooses terrain, dice rolls, and placement.
 */
public class RandomBoardGenerator implements BoardGenerator {

    public void run(GameBoard board, HashSet<Coordinate> validSpaces, HashSet<Coordinate> tradingPosts) {
        //Prep all of the helper classes to generate map features.
        TerrainGenerator terrainGenerator = new TerrainGenerator(validSpaces.size());
        TokenGenerator tokenGenerator = new TokenGenerator(validSpaces.size() - 1); //Don't generate a token for the desert.
        TradeGenerator tradeGenerator = new TradeGenerator(tradingPosts.size());
        terrainGenerator.randomize();
        tokenGenerator.randomize();
        tradeGenerator.randomize();
        Iterator<Terrain> terrain = terrainGenerator.iterator();
        Iterator<DiceRoll> tokens = tokenGenerator.iterator();
        Iterator<TradingPost> posts = tradeGenerator.iterator();

        //Use the valid hexagons to find all valid vertices and edges, as well as adjacent tiles that are beaches.
        HashSet<Coordinate> validVertices = board.getValidVertices(validSpaces);
        HashSet<Coordinate> validEdges = board.getValidEdges(validSpaces);
        HashSet<Coordinate> beachTiles = board.getBeachTiles(validSpaces);

        //Generate and place playable hexagons
        for (Coordinate c : validSpaces) {
            Terrain t = terrain.next();
            ResourceTile tile = null;
            if (t.equals(Terrain.Desert)) {
                tile = new ResourceTile(t, null);
                tile.placeRobber();
            } else {
                tile = new ResourceTile(t, tokens.next());
                board.setDiceRollCoordinate(tile.getDiceRoll(), c);
            }
            board.setTile(c, tile);
        }

        //Place all of the empty features that fill the rest of the valid map.
        for (Coordinate c : validVertices) {
            board.setBuilding(c, new EmptyTown());
        }
        for (Coordinate c : validEdges) {
            board.setPath(c, new EmptyPath());
        }

        // Place all beaches, checking for surrounding tiles.
        for (Coordinate c : beachTiles) {
            //Find surrounding tiles and save the directions.
            HashSet<Direction> found = board.getAdjacentResourceTiles(c);
            BeachTile tile = null;
            if (tradingPosts.contains(c)) {
                TradeTile trade = new TradeTile(Direction.getAverage(found), found.size(), posts.next());
                for (Coordinate vertex : trade.getTradingPostCoordinates()) {
                    board.setTradingPostCoordinate(vertex, trade.getTradingPost());
                }
                tile = trade;
            } else
                tile = new BeachTile(Direction.getAverage(found), found.size());
            board.setTile(c, tile);
        }

    }
}
