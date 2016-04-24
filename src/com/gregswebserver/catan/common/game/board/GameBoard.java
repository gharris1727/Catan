package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.board.hexarray.CoordTransforms;
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
    private final Map<Coordinate, RoadSystem> roadSystems;
    private final PriorityQueue<RoadSystem> roadSystemLeaderboard;
    private Coordinate robberLocation;

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
        this.roadSystems = new HashMap<>();
        //Pass the priority queue an inverse comparator, because it by default minimizes the comparator.
        this.roadSystemLeaderboard = new PriorityQueue<>(new Comparator<RoadSystem>() {
            @Override
            public int compare(RoadSystem a, RoadSystem b) {
                return -1*a.compareTo(b);
            }
        });
        this.robberLocation = robberLocation;
    }

    public Dimension getSize() {
        return size;
    }

    public void moveRobber(Coordinate robberLocation) {
        ((ResourceTile) hexArray.spaces.get(this.robberLocation)).removeRobber();
        ((ResourceTile) hexArray.spaces.get(robberLocation)).placeRobber();
        this.robberLocation = robberLocation;
    }

    public Settlement buildSettlement(Coordinate c, Team team) {
        //Create the new settlement
        Settlement settlement = (Settlement) hexArray.setTown(c, new Settlement(team));
        //Update the adjacent edges in case they were separated by the settlement building.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromVertex(c).values())
            discoverRoadSystem(adjacent);
        //Return the created settlement to the caller.
        return settlement;
    }

    public City buildCity(Coordinate c, Team team) {
        //A city is just an upgrade, should not break any existing roads.
        return (City) hexArray.setTown(c, new City(team));
    }

    public void destroyTown(Coordinate c) {
        //Create a new empty town to go in place of the existing one.
        hexArray.setTown(c, new EmptyTown());
        //Update any adjacent roads in case they need to re-merge.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromVertex(c).values())
            discoverRoadSystem(adjacent);
    }

    public Road buildRoad(Coordinate c, Team team) {
        //Create the new road object, and put it in the array.
        Road road = (Road) hexArray.setPath(c, new Road(team));
        //Compute the connected path for the new road
        discoverRoadSystem(c);
        //Return the road that we created.
        return road;
    }

    public void destroyRoad(Coordinate c) {
        //Erase the path from the hexarray.
        hexArray.setPath(c, new EmptyPath());
        //Get the original path that we need to break up.
        RoadSystem path = roadSystems.get(c);
        //Look at each of the neighbors of the edge being deleted.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromEdge(c).values()) {
            //If the RoadSystem for the neighbor has not been updated, we need to update it.
            if (roadSystems.get(adjacent) == path)
                discoverRoadSystem(adjacent);
        }
    }

    private void discoverRoadSystem(Coordinate origin) {
        //Get the path at the origin coordinate
        Path originPath = hexArray.getPath(origin);
        //If there is no edge, or it is a non-team path then we shouldn't process anything.
        if (originPath != null && originPath.getTeam() != Team.None) {
            //Create the new path
            RoadSystem roadSystem = new RoadSystem(hexArray, origin);
            //Add this path to the overall list of roadSystems.
            roadSystemLeaderboard.add(roadSystem);
            //For every path that is a member, we need to update their path pointers.
            for (Path p : roadSystem.getPaths()) {
                //Get the path that is associated with that edge.
                RoadSystem existing = roadSystems.get(p.getPosition());
                //If there was an existing path, it is now invalid so remove it from the priority queue.
                if (existing != null)
                    roadSystemLeaderboard.remove(existing);
                //Now map this edge to the new path.
                this.roadSystems.put(p.getPosition(), roadSystem);
            }
        }
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
        for (Coordinate vertex : CoordTransforms.getAdjacentVerticesFromEdge(c).values()) {
            Town t  = hexArray.getTown(vertex);
            if (t != null && t.getTeam() == team)
                return true;
            if (t == null || t instanceof EmptyTown) {
                for (Coordinate edge : CoordTransforms.getAdjacentEdgesFromVertex(vertex).values()) {
                    Path p = hexArray.getPath(edge);
                    if (p instanceof Road && p.getTeam() == team)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean canBuildSettlement(Coordinate c, Team team) {
        if (!canBuildInitialSettlement(c))
            return false;
        Map<Direction, Coordinate> adjacent = CoordTransforms.getAdjacentEdgesFromVertex(c);
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
        Map<Direction, Coordinate> adjacent = CoordTransforms.getAdjacentVerticesFromVertex(c);
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
        Map<Direction, Coordinate> adjacent = CoordTransforms.getAdjacentVerticesFromVertex(c);
        for (Coordinate a : adjacent.values()) {
            Town t = hexArray.getTown(a);
            if (t instanceof Settlement || t instanceof City)
                return false;
        }
        return true;
    }

    public Set<Tile> getAdjacentTiles(Coordinate vertex) {
        Set<Tile> tiles = new HashSet<>();
        for (Coordinate a : CoordTransforms.getAdjacentSpacesFromVertex(vertex).values()) {
            tiles.add(hexArray.getTile(a));
        }
        return tiles;
    }

    public Set<Town> getAdjacentTowns(Coordinate space) {
        Set<Town> towns = new HashSet<>();
        for (Coordinate vertex : CoordTransforms.getAdjacentVerticesFromSpace(space).values()) {
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
        return robberLocation.equals(gameBoard.robberLocation);
    }

    @Override
    public String toString() {
        return "GameBoard{" +
                "size=" + size +
                ", hexArray=" + hexArray +
                ", diceRolls=" + diceRolls +
                ", tradingPosts=" + tradingPosts +
                ", robberLocation=" + robberLocation +
                '}';
    }
}
