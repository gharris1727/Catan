package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.hexarray.HexagonalArray;
import com.gregswebserver.catan.common.game.board.paths.EmptyPath;
import com.gregswebserver.catan.common.game.board.paths.Path;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.board.tiles.TradeTile;
import com.gregswebserver.catan.common.game.board.towns.City;
import com.gregswebserver.catan.common.game.board.towns.EmptyTown;
import com.gregswebserver.catan.common.game.board.towns.Settlement;
import com.gregswebserver.catan.common.game.board.towns.Town;
import com.gregswebserver.catan.common.game.gameplay.enums.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.enums.Team;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard {

    private final Dimension size;
    private final HexagonalArray hexArray;
    private final Map<DiceRoll, List<Coordinate>> diceRolls;
    private final List<Coordinate> tradingPosts;
    private final Stack<Coordinate> robberLocations;

    public GameBoard(
            Dimension size,
            HexagonalArray hexArray,
            Map<DiceRoll, List<Coordinate>> diceRolls,
            List<Coordinate> tradingPosts,
            Coordinate robberLocation) {
        this.size = size;
        this.hexArray = hexArray;
        this.diceRolls = diceRolls;
        this.tradingPosts = tradingPosts;
        this.robberLocations = new Stack<>();
        this.robberLocations.push(robberLocation);
    }

    public Dimension getSize() {
        return size;
    }

    public void moveRobber(Coordinate coordinate) {
        ((ResourceTile) hexArray.spaces.get(coordinate)).placeRobber();
        ((ResourceTile) hexArray.spaces.get(robberLocations.peek())).removeRobber();
        robberLocations.push(coordinate);
    }

    public void undoRobber() {
        if (robberLocations.size() > 1) {
            ((ResourceTile) hexArray.spaces.get(robberLocations.peek())).removeRobber();
            robberLocations.pop();
            ((ResourceTile) hexArray.spaces.get(robberLocations.peek())).placeRobber();
        }
    }

    public Settlement buildSettlement(Coordinate c, Team team) {
        return (Settlement) hexArray.setTown(c, new Settlement(team));
    }

    public City buildCity(Coordinate c, Team team) {
        return (City) hexArray.setTown(c, new City(team));
    }

    public void destroyTown(Coordinate c) {
        hexArray.setTown(c, new EmptyTown());
    }

    public Road buildRoad(Coordinate c, Team team) {
        return (Road) hexArray.setPath(c, new Road(team));
    }

    public void destroyRoad(Coordinate c) {
        hexArray.setPath(c, new EmptyPath());
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

    public boolean canBuildRoad(Coordinate c, Team team) {
        if (c == null)
            return false;
        Path e = hexArray.getPath(c);
        if (!(e instanceof EmptyPath))
            return false;
        Map<Direction, Coordinate> adjacentEdges = hexArray.getAdjacentEdgesFromEdge(c);
        for (Coordinate a : adjacentEdges.values()) {
            Path p = hexArray.getPath(a);
            if (p instanceof Road && p.getTeam() == team)
                return true;
        }
        Map<Direction, Coordinate> adjacentVertices = hexArray.getAdjacentVerticesFromEdge(c);
        for (Coordinate a : adjacentVertices.values()) {
            Town t = hexArray.getTown(a);
            if ((t instanceof Settlement || t instanceof City) && t.getTeam() == team)
                return true;
        }
        return false;
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
        Set<Tile> tiles = new HashSet<>();
        for (Coordinate a : hexArray.getAdjacentSpacesFromVertex(vertex).values()) {
            tiles.add(hexArray.getTile(a));
        }
        return tiles;
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

    public Set<TradingPostType> getTrades(Team team) {
        Set<TradingPostType> trades = EnumSet.noneOf(TradingPostType.class);
        for (Coordinate tradeSpace : tradingPosts) {
            TradeTile tradeTile = (TradeTile) hexArray.getTile(tradeSpace);
            for (Coordinate tradeTown : tradeTile.getTradingPostCoordinates()) {
                Town town = hexArray.getTown(tradeTown);
                if (town != null && town.getTeam() == team)
                    trades.add(tradeTile.getTradingPostType());
            }
        }
        return trades;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameBoard gameBoard = (GameBoard) o;

        if (!size.equals(gameBoard.size)) return false;
        if (!hexArray.equals(gameBoard.hexArray)) return false;
        if (!diceRolls.equals(gameBoard.diceRolls)) return false;
        if (!tradingPosts.equals(gameBoard.tradingPosts)) return false;
        return robberLocations.equals(gameBoard.robberLocations);
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                "size=" + size +
                ", hexArray=" + hexArray +
                ", diceRolls=" + diceRolls +
                ", tradingPosts=" + tradingPosts +
                ", robberLocations=" + robberLocations +
                '}';
    }
}
