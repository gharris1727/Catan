package catan.common.game.board;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.board.hexarray.CoordTransforms;
import catan.common.game.board.hexarray.Coordinate;
import catan.common.game.board.hexarray.HexagonalArray;
import catan.common.game.board.paths.EmptyPath;
import catan.common.game.board.paths.Path;
import catan.common.game.board.paths.Road;
import catan.common.game.board.tiles.ResourceTile;
import catan.common.game.board.tiles.Tile;
import catan.common.game.board.tiles.TradeTile;
import catan.common.game.board.towns.City;
import catan.common.game.board.towns.EmptyTown;
import catan.common.game.board.towns.Settlement;
import catan.common.game.board.towns.Town;
import catan.common.game.gameplay.trade.TradingPostType;
import catan.common.game.gamestate.DiceRoll;
import catan.common.game.teams.TeamColor;

import java.awt.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Created by Greg on 8/10/2014.
 * Game Board handling all different functions with adding and moving pieces.
 */
public class GameBoard implements ReversibleEventConsumer<BoardEvent> {

    final Dimension size;
    final HexagonalArray hexArray;
    final Map<DiceRoll, Set<Coordinate>> diceRolls;
    final Set<Coordinate> tradingPosts;
    final Stack<BoardEvent> history;
    final Stack<Coordinate> robberLocations;

    public GameBoard(
            Dimension size,
            HexagonalArray hexArray,
            Map<DiceRoll, Set<Coordinate>> diceRolls,
            Set<Coordinate> tradingPosts,
            Coordinate initialRobberPosition) {
        this.size = size;
        this.hexArray = hexArray;
        this.diceRolls = diceRolls;
        this.tradingPosts = tradingPosts;
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

    public Set<Coordinate> getSpaceCoordinates() {
        return hexArray.getSpaceCoordinates();
    }

    public Set<Coordinate> getEdgeCoordinates() {
        return hexArray.getEdgeCoordinates();
    }

    public Set<Coordinate> getVertexCoordinates() {
        return hexArray.getVertexCoordinates();
    }

    public void eachTile(Consumer<Tile> action) {
        for (Coordinate space : getSpaceCoordinates()) {
            action.accept(getTile(space));
        }
    }

    public void eachPath(Consumer<Path> action) {
        for (Coordinate edge : getEdgeCoordinates()) {
            action.accept(getPath(edge));
        }
    }

    public void eachTown(Consumer<Town> action) {
        for (Coordinate vertex : getVertexCoordinates()) {
            action.accept(getTown(vertex));
        }
    }

    public Set<Coordinate> getActiveTiles(DiceRoll roll) {
        return Collections.unmodifiableSet(
            roll == DiceRoll.Seven ? Collections.emptySet() : diceRolls.get(roll)
        );
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
                    hexArray.setTown((Coordinate) event.getPayload(), new EmptyTown());
                    break;
                case Place_City:
                    hexArray.setTown((Coordinate) event.getPayload(), new Settlement(event.getOrigin()));
                    break;
                case Place_Road:
                    hexArray.setPath((Coordinate) event.getPayload(), new EmptyPath());
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public EventConsumerProblem test(BoardEvent event) {
        Coordinate c = (Coordinate) event.getPayload();
        //If the user didnt specify a coordinate
        if (c == null)
            return new EventConsumerProblem("Location not specified");
        switch (event.getType()) {
            case Place_Robber:
                //If the coordinate doesn't refer to a resource tile
                if (!(hexArray.getTile(c) instanceof ResourceTile))
                    return new EventConsumerProblem("Location invalid");
                //If the location was already robbed.
                if (c == robberLocations.peek())
                    return new EventConsumerProblem("Location already occupied");
                break;
            case Place_Settlement:
                //Flag to track whether there is a valid path nearby to build off of
                boolean foundRoad = false;
                for (Coordinate a : CoordTransforms.getAdjacentEdgesFromVertex(c).values()) {
                    Path p = hexArray.getPath(a);
                    foundRoad = foundRoad || (p instanceof Road && p.getTeam() == event.getOrigin());
                }
                //If there was no nearby roads
                if (!foundRoad)
                    return new EventConsumerProblem("No adjoining road");
                //Intentionally flow into the next case.
            case Place_Outpost:
                //If the town is not ready for settling
                if (!(hexArray.getTown(c) instanceof EmptyTown))
                    return new EventConsumerProblem("Location invalid");
                //Look over all adjacent town locations for a nearby town.
                for (Coordinate town : CoordTransforms.getAdjacentVerticesFromVertex(c).values()) {
                    Town t = hexArray.getTown(town);
                    //If there was a settled town nearby.
                    if (t instanceof Settlement || t instanceof City)
                        return new EventConsumerProblem("Location too close to other town");
                }
                break;
            case Place_City:
                Town e = hexArray.getTown(c);
                //If the town is not one of our settlements
                if (!(e instanceof Settlement) || e.getTeam() != event.getOrigin())
                    return new EventConsumerProblem("Invalid location");
                return null;
            case Place_Road:
                TeamColor teamColor = event.getOrigin();
                //If the path is not ready for paving
                if (!(hexArray.getPath(c) instanceof EmptyPath))
                    return new EventConsumerProblem("Invalid location");
                //Go over all adjacent towns
                for (Coordinate vertex : CoordTransforms.getAdjacentVerticesFromEdge(c).values()) {
                    Town t  = hexArray.getTown(vertex);
                    //If there is a friendly town there, great!
                    if (t != null && t.getTeam() == teamColor)
                        return null;
                    //If it is an unsettled town, check for incoming roads.
                    if (t instanceof EmptyTown) {
                        for (Coordinate edge : CoordTransforms.getAdjacentEdgesFromVertex(vertex).values()) {
                            Path p = hexArray.getPath(edge);
                            //If there is a friendly road, great!
                            if (p instanceof Road && p.getTeam() == teamColor)
                                return null;
                        }
                    }
                }
                //Otherwise we couldnt find any friendly people nearby.
                return new EventConsumerProblem("Location too far from civilization");
        }
        return null;
    }

    @Override
    public void execute(BoardEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(event, problem);
        TeamColor origin = event.getOrigin();
        Coordinate c = (Coordinate) event.getPayload();
        try {
            history.push(event);
            switch (event.getType()) {
                case Place_Robber:
                    Coordinate robberFrom = robberLocations.isEmpty() ? null : robberLocations.peek();
                    robberLocations.push(c);
                    moveRobber(robberFrom, c);
                    break;
                case Place_Outpost:
                case Place_Settlement:
                    //Create the new settlement
                    hexArray.setTown(c, new Settlement(origin));
                    break;
                case Place_City:
                    //A city is just an upgrade, should not break any existing roads.
                    hexArray.setTown(c, new City(origin));
                    break;
                case Place_Road:
                    //Create the new road object, and put it in the array.
                    hexArray.setPath(c, new Road(origin));
                    break;
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    private void moveRobber(Coordinate from, Coordinate to) {
        if (from != null)
            ((ResourceTile) hexArray.getTile(from)).removeRobber();
        if (to != null)
            ((ResourceTile) hexArray.getTile(to)).placeRobber();
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
        if (!history.equals(gameBoard.history)) return false;
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
