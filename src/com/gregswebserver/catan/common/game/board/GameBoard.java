package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
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
import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;
import com.gregswebserver.catan.common.game.gameplay.enums.TradingPostType;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard implements ReversibleEventConsumer<BoardEvent> {

    private final Dimension size;
    private final HexagonalArray hexArray;
    private final Map<DiceRoll, List<Coordinate>> diceRolls;
    private final List<Coordinate> tradingPosts;
    private final Map<Coordinate, RoadSystem> roadSystems;
    private final PriorityQueue<RoadSystem> roadSystemLeaderboard;
    private final Stack<BoardEvent> history;
    private final Stack<Coordinate> robberLocations;

    public GameBoard(
            Dimension size,
            HexagonalArray hexArray,
            Map<DiceRoll, List<Coordinate>> diceRolls,
            List<Coordinate> tradingPosts,
            Coordinate initialRobberPosition) {
        this.size = size;
        this.hexArray = hexArray;
        this.diceRolls = diceRolls;
        this.tradingPosts = tradingPosts;
        this.roadSystems = new HashMap<>();
        //Pass the priority queue an inverse comparator, because it by default minimizes the comparator.
        //TODO: implement Road System scoring.
        this.roadSystemLeaderboard = new PriorityQueue<>(new Comparator<RoadSystem>() {
            @Override
            public int compare(RoadSystem a, RoadSystem b) {
                return -1*a.compareTo(b);
            }
        });
        this.history = new Stack<>();
        this.robberLocations = new Stack<>();
        this.robberLocations.push(initialRobberPosition);
    }

    public Dimension getSize() {
        return size;
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

    public Set<TradingPostType> getTrades(TeamColor teamColor) {
        Set<TradingPostType> trades = EnumSet.noneOf(TradingPostType.class);
        for (Coordinate tradeSpace : tradingPosts) {
            TradeTile tradeTile = (TradeTile) hexArray.getTile(tradeSpace);
            for (Coordinate tradeTown : tradeTile.getTradingPostCoordinates()) {
                Town town = hexArray.getTown(tradeTown);
                if (town != null && town.getTeam() == teamColor)
                    trades.add(tradeTile.getTradingPostType());
            }
        }
        return trades;
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

    public Set<Tile> getAdjacentTiles(Coordinate vertex) {
        Set<Tile> tiles = new HashSet<>();
        for (Coordinate a : CoordTransforms.getAdjacentSpacesFromVertex(vertex).values())
            tiles.add(hexArray.getTile(a));
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

    private void discoverRoadSystem(Coordinate origin) {
        //Get the path at the origin coordinate
        Path originPath = hexArray.getPath(origin);
        //If there is no edge, or it is a non-teamColor path then we shouldn't process anything.
        if (originPath != null && originPath.getTeam() != TeamColor.None) {
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

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        BoardEvent event = history.pop();
        try {
            switch (event.getType()) {
                case Place_Robber:
                    Coordinate robberFrom = robberLocations.isEmpty() ? null : robberLocations.pop();
                    Coordinate robberTo = robberLocations.isEmpty() ? null : robberLocations.peek();
                    moveRobber(robberFrom, robberTo);
                    break;
                case Place_Outpost:
                case Place_Settlement:
                    destroyTown((Coordinate) event.getPayload());
                    break;
                case Place_City:
                    placeSettlement(event.getOrigin(), (Coordinate) event.getPayload());
                    break;
                case Place_Road:
                    destroyRoad((Coordinate) event.getPayload());
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(BoardEvent event) throws EventConsumerException{
        switch (event.getType()) {
            case Place_Robber:
                if (!canPlaceRobber((Coordinate) event.getPayload()))
                    throw new EventConsumerException("Cannot place robber");
                break;
            case Place_Outpost:
                if (!canPlaceOutpost((Coordinate) event.getPayload()))
                    throw new EventConsumerException("Cannot place outpost");
                break;
            case Place_Settlement:
                if (!canPlaceSettlement(event.getOrigin(), (Coordinate) event.getPayload()))
                    throw new EventConsumerException("Cannot place settlement");
                break;
            case Place_City:
                if (!canPlaceCity(event.getOrigin(), (Coordinate) event.getPayload()))
                    throw new EventConsumerException("Cannot place city");
                break;
            case Place_Road:
                if (!canBuildRoad(event.getOrigin(), (Coordinate) event.getPayload()))
                    throw new EventConsumerException("Cannot place road");
                break;
        }
    }

    @Override
    public void execute(BoardEvent event) throws EventConsumerException {
        test(event);
        try {
            history.push(event);
            switch (event.getType()) {
                case Place_Robber:
                    Coordinate robberFrom = robberLocations.isEmpty() ? null : robberLocations.peek();
                    Coordinate robberTo = (Coordinate) event.getPayload();
                    robberLocations.push(robberTo);
                    moveRobber(robberFrom, robberTo);
                    break;
                case Place_Outpost:
                case Place_Settlement:
                    placeSettlement(event.getOrigin(), (Coordinate) event.getPayload());
                    break;
                case Place_City:
                    placeCity(event.getOrigin(), (Coordinate) event.getPayload());
                    break;
                case Place_Road:
                    placeRoad(event.getOrigin(), (Coordinate) event.getPayload());
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    private boolean canPlaceRobber(Coordinate c) {
        return c != robberLocations.peek() && hexArray.getTile(c) instanceof ResourceTile;
    }

    private void moveRobber(Coordinate from, Coordinate to) {
        if (from != null)
            ((ResourceTile) hexArray.spaces.get(from)).removeRobber();
        if (to != null)
            ((ResourceTile) hexArray.spaces.get(to)).placeRobber();
    }

    private boolean canPlaceOutpost(Coordinate coord) {
        Town e = hexArray.getTown(coord);
        if (!(e instanceof EmptyTown))
            return false;
        Map<Direction, Coordinate> adjacent = CoordTransforms.getAdjacentVerticesFromVertex(coord);
        for (Coordinate a : adjacent.values()) {
            Town t = hexArray.getTown(a);
            if (t instanceof Settlement || t instanceof City)
                return false;
        }
        return true;
    }

    private boolean canPlaceSettlement(TeamColor teamColor, Coordinate coord) {
        if (!canPlaceOutpost(coord))
            return false;
        Map<Direction, Coordinate> adjacent = CoordTransforms.getAdjacentEdgesFromVertex(coord);
        for (Coordinate a : adjacent.values()) {
            Path p = hexArray.getPath(a);
            if (p instanceof Road && p.getTeam() == teamColor)
                return true;
        }
        return false;
    }

    private void placeSettlement(TeamColor teamColor, Coordinate coord) {
        //Create the new settlement
        hexArray.setTown(coord, new Settlement(teamColor));
        //Update the adjacent edges in case they were separated by the settlement building.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromVertex(coord).values())
            discoverRoadSystem(adjacent);
    }

    private boolean canPlaceCity(TeamColor teamColor, Coordinate coord) {
        Town e = hexArray.getTown(coord);
        if (!(e instanceof Settlement) || e.getTeam() != teamColor)
            return false;
        Map<Direction, Coordinate> adjacent = CoordTransforms.getAdjacentVerticesFromVertex(coord);
        for (Coordinate a : adjacent.values()) {
            Town t = hexArray.getTown(a);
            if (t instanceof Settlement || t instanceof City)
                return false;
        }
        return true;
    }

    private void placeCity(TeamColor teamColor, Coordinate coord) {
        //A city is just an upgrade, should not break any existing roads.
        hexArray.setTown(coord, new City(teamColor));
    }

    private void destroyTown(Coordinate coord) {
        //Create a new empty town to go in place of the existing one.
        hexArray.setTown(coord, new EmptyTown());
        //Update any adjacent roads in case they need to re-merge.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromVertex(coord).values())
            discoverRoadSystem(adjacent);
    }

    private boolean canBuildRoad(TeamColor teamColor, Coordinate coord) {
        Path e = hexArray.getPath(coord);
        if (!(e instanceof EmptyPath))
            return false;
        for (Coordinate vertex : CoordTransforms.getAdjacentVerticesFromEdge(coord).values()) {
            Town t  = hexArray.getTown(vertex);
            if (t != null && t.getTeam() == teamColor)
                return true;
            if (t == null || t instanceof EmptyTown) {
                for (Coordinate edge : CoordTransforms.getAdjacentEdgesFromVertex(vertex).values()) {
                    Path p = hexArray.getPath(edge);
                    if (p instanceof Road && p.getTeam() == teamColor)
                        return true;
                }
            }
        }
        return false;
    }

    private void placeRoad(TeamColor teamColor, Coordinate coord) {
        //Create the new road object, and put it in the array.
        hexArray.setPath(coord, new Road(teamColor));
        //Compute the connected path for the new road
        discoverRoadSystem(coord);
    }

    private void destroyRoad(Coordinate coord) {
        //Erase the path from the hexarray.
        hexArray.setPath(coord, new EmptyPath());
        //Get the original path that we need to break up.
        RoadSystem path = roadSystems.get(coord);
        //Look at each of the neighbors of the edge being deleted.
        for (Coordinate adjacent : CoordTransforms.getAdjacentEdgesFromEdge(coord).values()) {
            //If the RoadSystem for the neighbor has not been updated, we need to update it.
            if (roadSystems.get(adjacent) == path)
                discoverRoadSystem(adjacent);
        }
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
