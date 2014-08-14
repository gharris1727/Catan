package com.gregswebserver.catan.game.gameplay.generator;

import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.buildings.OceanBuilding;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.hexarray.Direction;
import com.gregswebserver.catan.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.game.board.paths.OceanPath;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Terrain;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.DiceRoll;
import com.gregswebserver.catan.game.gameplay.enums.TradingPost;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Greg on 8/10/2014.
 * Tile generator that randomly chooses terrain, dice rolls, and placement.
 */
public class RandomBoardGenerator implements BoardGenerator {

    public void run(HexagonalArray<Tile, Path, Building> hexArray, HashSet<Coordinate> validSpaces, HashSet<Coordinate> tradingPosts) {

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
            validVertices.addAll(hexArray.getAdjacentVerticesFromSpace(spaceCoordinate));
            validEdges.addAll(hexArray.getAdjacentEdgesFromSpace(spaceCoordinate));
            beachTiles.addAll(hexArray.getAdjacentSpacesFromSpace(spaceCoordinate));
        }

        //Remove any beachTiles that are actually resourceTiles.
        beachTiles.removeAll(validSpaces);

        //Remove the valid/beach coordinates from all to find the ocean coordinates;
        HashSet<Coordinate> oceanTiles = hexArray.spaces.getAllCoordinates();
        oceanTiles.removeAll(validSpaces);
        oceanTiles.removeAll(beachTiles);
        HashSet<Coordinate> oceanVertices = hexArray.vertices.getAllCoordinates();
        oceanVertices.removeAll(validVertices);
        HashSet<Coordinate> oceanPaths = hexArray.edges.getAllCoordinates();
        oceanPaths.removeAll(validEdges);

        //Generate and place playable hexagons
        for (Coordinate c : validSpaces) {
            Terrain t = terrain.next();
            Tile tile = new Tile(t);
            if (t.equals(Terrain.Desert)) {
                tile.placeRobber();
            } else {
                tile.setDiceRoll(tokens.next());
            }
            hexArray.place(c, tile);
        }

        // Place all beaches, checking for surrounding tiles.
        for (Coordinate c : beachTiles) {
            //Find surrounding tiles and save the directions.
            HashSet<Direction> foundTiles = new HashSet<>();
            for (Direction d : Direction.values()) {
                try {
                    hexArray.getSpaceCoordinateFromSpace(c, d);
                    Tile t = hexArray.spaces.get(c);
                    //IMPORTANT: this check must happen AFTER tiles have been generated
                    //and BEFORE any ocean is generated. Otherwise everything is messed up.
                    if (t != null) {
                        foundTiles.add(d);
                    }
                } catch (Exception e) {
                    //Ignore any errors, just don't process that combination any further.
                }
            }
            //TODO: process directions to find beach type and face.
            hexArray.place(c, new Tile(Terrain.SingleBeach));
        }

        //Place all of the ocean features that fill the rest of the map.
        for (Coordinate c : oceanTiles) {
            hexArray.place(c, new Tile(Terrain.Ocean));
        }
        for (Coordinate c : oceanVertices) {
            hexArray.place(c, new OceanBuilding());
        }
        for (Coordinate c : oceanPaths) {
            hexArray.place(c, new OceanPath());
        }


    }
}
