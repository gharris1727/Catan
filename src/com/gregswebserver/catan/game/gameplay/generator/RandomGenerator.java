package com.gregswebserver.catan.game.gameplay.generator;

import com.gregswebserver.catan.game.board.buildings.Building;
import com.gregswebserver.catan.game.board.buildings.OceanBuilding;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.game.board.paths.OceanPath;
import com.gregswebserver.catan.game.board.paths.Path;
import com.gregswebserver.catan.game.board.tiles.Terrain;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.DiceGenerator;
import com.gregswebserver.catan.game.gameplay.DiceRoll;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Greg on 8/10/2014.
 * Tile generator that randomly chooses terrain, dice rolls, and placement.
 */
public class RandomGenerator extends Generator {

    public void run(HexagonalArray<Tile, Path, Building> hexArray, HashSet<Coordinate> validSpaces) {

        //Use the valid hexagons to find all valid vertices and edges.
        HashSet<Coordinate> validVertices = new HashSet<>();
        HashSet<Coordinate> validEdges = new HashSet<>();
        for (Coordinate spaceCoordinate : validSpaces) {
            validVertices.addAll(hexArray.getAdjacentVertices(spaceCoordinate));
            validEdges.addAll(hexArray.getAdjacentEdges(spaceCoordinate));
        }

        //Remove the valid coordinates from all to find the ocean coordinates;
        HashSet<Coordinate> oceanTiles = hexArray.spaces.getAllCoordinates();
        oceanTiles.removeAll(validSpaces);
        HashSet<Coordinate> oceanVertices = hexArray.vertices.getAllCoordinates();
        oceanVertices.removeAll(validVertices);
        HashSet<Coordinate> oceanPaths = hexArray.edges.getAllCoordinates();
        oceanPaths.removeAll(validEdges);

        //Generate dice rolls
        Iterator<DiceRoll> tileNumbers = new DiceGenerator(validSpaces.size()).iterator();
        Iterator<Coordinate> tileSpaces = validSpaces.iterator();

        //Generate and place playable hexagons
        Tile desert = new Tile(Terrain.Desert, null);
        desert.placeRobber();
        hexArray.place(tileSpaces.next(), desert);
        Coordinate tileCoordinate;
        while ((tileCoordinate = tileSpaces.next()) != null) {
            hexArray.place(tileCoordinate, new Tile(Terrain.random(), tileNumbers.next()));
        }

        //Place all of the ocean features that fill the rest of the map.
        for (Coordinate c : oceanTiles) {
            hexArray.place(c, new Tile(Terrain.Ocean, null));
        }
        for (Coordinate c : oceanVertices) {
            hexArray.place(c, new OceanBuilding());
        }
        for (Coordinate c : oceanPaths) {
            hexArray.place(c, new OceanPath());
        }


    }
}
