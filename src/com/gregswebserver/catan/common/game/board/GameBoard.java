package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Terrain;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.util.*;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard {

    private final Dimension size;
    private final HexagonalArray hexArray;
    private final HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;
    private final HashMap<Coordinate, TradingPostType> tradingPosts;
    private Coordinate robberLocation;

    public GameBoard(Dimension size) {
        this.size = size;
        hexArray = new HexagonalArray(size.width, size.height);
        diceRollCoordinates = new HashMap<>();
        tradingPosts = new HashMap<>();
    }

    public void setDiceRollCoordinate(DiceRoll diceRoll, Coordinate c) {
        ArrayList<Coordinate> coordinates;
        if (!diceRollCoordinates.containsKey(diceRoll)) {
            coordinates = new ArrayList<>();
            diceRollCoordinates.put(diceRoll, coordinates);
        } else {
            coordinates = diceRollCoordinates.get(diceRoll);
        }
        coordinates.add(c);
    }

    public void setTradingPostCoordinate(Coordinate coordinate, TradingPostType tradingPostType) {
        tradingPosts.put(coordinate, tradingPostType);
    }

    public Dimension getSize() {
        return size;
    }

    public void moveRobber(Coordinate coordinate) {
        ResourceTile newTile = (ResourceTile) hexArray.spaces.get(coordinate);
        ResourceTile oldTile = (ResourceTile) hexArray.spaces.get(robberLocation);
        oldTile.removeRobber();
        newTile.placeRobber();
        robberLocation = coordinate;
    }

    public void setTile(Coordinate c, Tile t) {
        hexArray.setTile(c, t);
        if (t instanceof ResourceTile && ((ResourceTile) t).getTerrain() == Terrain.Desert)
            robberLocation = c;
    }

    public void setBuilding(Coordinate c, Town b) {
        hexArray.setTown(c, b);
    }

    public void setPath(Coordinate c, Path p) {
        hexArray.setPath(c, p);
    }

    public Path getPath(Coordinate c) {
        return hexArray.getPath(c);
    }

    public Tile getTile(Coordinate c) {
        return hexArray.getTile(c);
    }

    public Town getBuilding(Coordinate c) {
        return hexArray.getTown(c);
    }

    public Map<Coordinate, Tile> getTileMap() {
        return hexArray.spaces.toMap();
    }

    public Map<Coordinate, Path> getPathMap() {
        return hexArray.edges.toMap();
    }

    public Map<Coordinate, Town> getTownMap() {
        return hexArray.vertices.toMap();
    }

    public Set<Coordinate> getBeachTiles(Set<Coordinate> landTiles) {
        Set<Coordinate> out = new HashSet<>();
        for (Coordinate c : landTiles)
            out.addAll(hexArray.getAdjacentSpacesFromSpace(c).values());
        out.removeAll(landTiles);
        return out;
    }

    public Set<Coordinate> getAdjacentVertices(Coordinate coordinate) {
        return new HashSet<>(hexArray.getAdjacentVerticesFromSpace(coordinate).values());
    }

    public Set<Coordinate> getAdjacentEdges(Coordinate coordinate) {
        return new HashSet<>(hexArray.getAdjacentEdgesFromSpace(coordinate).values());
    }

    public Set<Direction> getDirectionsOfAdjacentTiles(Coordinate coordinate) {
        Set<Direction> found = EnumSet.noneOf(Direction.class);
        for (Map.Entry<Direction, Coordinate> e : hexArray.getAdjacentSpacesFromSpace(coordinate).entrySet()) {
            Tile t = getTile(e.getValue());
            if (t != null && t instanceof ResourceTile)
                found.add(e.getKey());
        }
        return found;
    }
}
