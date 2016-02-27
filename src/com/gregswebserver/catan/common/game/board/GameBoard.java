package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.paths.EmptyPath;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard {

    private final Dimension size;
    private final HexagonalArray hexArray;
    private final Map<DiceRoll, List<Coordinate>> diceRolls;
    private final Map<Coordinate, TradingPostType> tradingPosts;
    private Coordinate robberLocation;

    public GameBoard(
            Dimension size,
            HexagonalArray hexArray,
            Map<DiceRoll, List<Coordinate>> diceRolls,
            Map<Coordinate, TradingPostType> tradingPosts,
            Coordinate robberLocation) {
        this.size = size;
        this.hexArray = hexArray;
        this.diceRolls = diceRolls;
        this.tradingPosts = tradingPosts;
        this.robberLocation = robberLocation;
    }

    public Dimension getSize() {
        return size;
    }

    public void moveRobber(Coordinate coordinate) {
        ((ResourceTile) hexArray.spaces.get(coordinate)).placeRobber();
        if (robberLocation != null)
            ((ResourceTile) hexArray.spaces.get(robberLocation)).removeRobber();
        robberLocation = coordinate;
    }

    public Settlement buildSettlement(Coordinate c, Team team) {
        Settlement settlement = new Settlement(team);
        hexArray.setTown(c, settlement);
        return settlement;
    }

    public City buildCity(Coordinate c, Team team) {
        City city = new City(team);
        hexArray.setTown(c, city);
        return city;
    }

    public Road buildRoad(Coordinate c, Team team) {
        Road road = new Road(team);
        hexArray.setPath(c, road);
        return road;
    }

    public BoardObject refresh(BoardObject object) {
        if (object instanceof Tile)
            return hexArray.getTile(object.getPosition());
        else if (object instanceof Path)
            return hexArray.getPath(object.getPosition());
        else if (object instanceof Town)
            return hexArray.getTown(object.getPosition());
        return object;
    }

    public Path getPath(Coordinate c) {
        return hexArray.getPath(c);
    }

    public Tile getTile(Coordinate c) {
        return hexArray.getTile(c);
    }

    public Town getTown(Coordinate c) {
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

    public List<Coordinate> getActiveTiles(DiceRoll roll) {
        return diceRolls.get(roll);
    }

    public boolean canRobTile(Coordinate c) {
        if (c == null)
            return false;
        Tile t = hexArray.getTile(c);
        return t instanceof ResourceTile && !((ResourceTile) t).hasRobber();
    }

    public boolean canBuildRoad(Coordinate c) {
        if (c == null)
            return false;
        Path e = hexArray.getPath(c);
        return e instanceof EmptyPath;
    }

    public boolean canBuildSettlement(Coordinate c, Team team) {
        if (!canBuildInitialSettlement(c))
            return false;
        Map<Direction, Coordinate> adjacent = hexArray.getAdjacentEdgesFromVertex(c);
        for (Coordinate a : adjacent.values()) {
            Path p = hexArray.getPath(a);
            if (p instanceof Road && p.getTeam() == team)
                return true;
        }
        return false;
    }

    public boolean canBuildInitialSettlement(Coordinate c) {
        if (c == null)
            return false;
        Town e = hexArray.getTown(c);
        if (!(e instanceof EmptyTown))
            return false;
        Map<Direction, Coordinate> adjacent = hexArray.getAdjacentVerticesFromVertex(c);
        for (Coordinate a : adjacent.values()) {
            Town t = hexArray.getTown(a);
            if (t instanceof Settlement || t instanceof City)
                return false;
        }
        return true;
    }

    public boolean canBuildCity(Coordinate c, Team team) {
        if (c == null)
            return false;
        Town e = hexArray.getTown(c);
        if (!(e instanceof Settlement) || e.getTeam() != team)
            return false;
        Map<Direction, Coordinate> adjacent = hexArray.getAdjacentVerticesFromVertex(c);
        for (Coordinate a : adjacent.values()) {
            Town t = hexArray.getTown(a);
            if (t instanceof Settlement || t instanceof City)
                return false;
        }
        return true;
    }

    public Set<Tile> getAdjacentTiles(Coordinate vertex) {
        return hexArray.getAdjacentSpacesFromVertex(vertex).values().stream()
                .map(hexArray::getTile)
                .collect(Collectors.toSet());
    }

    public Set<Town> getAdjacentTowns(Coordinate space) {
        Set<Town> towns = new HashSet<>();
        for (Coordinate vertex : hexArray.getAdjacentVerticesFromSpace(space).values()) {
            Town town = hexArray.getTown(vertex);
            if (town != null)
                towns.add(town);
        }
        return towns;
    }
}
