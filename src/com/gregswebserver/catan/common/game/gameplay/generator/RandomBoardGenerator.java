package com.gregswebserver.catan.common.game.gameplay.generator;

import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.buildings.OceanBuilding;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.Direction;
import com.gregswebserver.catan.common.game.board.hexarray.IllegalDirectionException;
import com.gregswebserver.catan.common.game.board.paths.OceanPath;
import com.gregswebserver.catan.common.game.board.tiles.*;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
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
        HashSet<Coordinate> validVertices = new HashSet<>();
        HashSet<Coordinate> validEdges = new HashSet<>();
        HashSet<Coordinate> beachTiles = new HashSet<>();
        for (Coordinate spaceCoordinate : validSpaces) {
            validVertices.addAll(board.hexArray.getAdjacentVerticesFromSpace(spaceCoordinate).values());
            validEdges.addAll(board.hexArray.getAdjacentEdgesFromSpace(spaceCoordinate).values());
            beachTiles.addAll(board.hexArray.getAdjacentSpacesFromSpace(spaceCoordinate).values());
        }

        //Remove any beachTiles that are actually resourceTiles.
        beachTiles.removeAll(validSpaces);

        //Remove the valid/beach coordinates from all to find the ocean coordinates;
        HashSet<Coordinate> oceanTiles = board.hexArray.spaces.getAllCoordinates();
        oceanTiles.removeAll(validSpaces);
        oceanTiles.removeAll(beachTiles);
        HashSet<Coordinate> oceanVertices = board.hexArray.vertices.getAllCoordinates();
        oceanVertices.removeAll(validVertices);
        HashSet<Coordinate> oceanPaths = board.hexArray.edges.getAllCoordinates();
        oceanPaths.removeAll(validEdges);

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
            board.hexArray.place(c, tile);
        }

        //Place all of the ocean features that fill the rest of the map.
        for (Coordinate c : oceanTiles) {
            board.hexArray.place(c, new OceanTile());
        }
        for (Coordinate c : oceanVertices) {
            board.hexArray.place(c, new OceanBuilding());
        }
        for (Coordinate c : oceanPaths) {
            board.hexArray.place(c, new OceanPath());
        }

        // Place all beaches, checking for surrounding tiles.
        for (Coordinate c : beachTiles) {
            //Find surrounding tiles and save the directions.
            HashSet<Direction> foundTiles = new HashSet<>();
            for (Direction d : Direction.values()) {
                try {
                    Coordinate found = board.hexArray.getSpaceCoordinateFromSpace(c, d);
                    Tile t = board.hexArray.spaces.get(found);
                    //IMPORTANT: this check must happen AFTER tiles have been generated
                    //and BEFORE any ocean is generated. Otherwise everything is messed up.
                    if (t != null && t instanceof ResourceTile) {
                        foundTiles.add(d);
                    }
                } catch (Exception e) {
                    //Ignore any errors, just don't process that combination any further.
                }
            }
            BeachTile tile = null;
            if (tradingPosts.contains(c)) {
                TradeTile trade = new TradeTile(foundTiles.size(), Direction.getAverage(foundTiles), posts.next());
                HashSet<Direction> directions = trade.getTradingPostDirections();
                for (Direction d : directions) {
                    try {
                        board.setTradingPostCoordinate(board.hexArray.getVertexCoordinateFromSpace(c, d), trade.getTradingPost());
                    } catch (IllegalDirectionException e) {
                        //uh.
                    }
                }
                tile = trade;
            } else
                tile = new BeachTile(foundTiles.size(), Direction.getAverage(foundTiles));
            board.hexArray.place(c, tile);
        }

    }
}
