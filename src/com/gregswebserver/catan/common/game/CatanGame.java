package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.board.buildings.Building;
import com.gregswebserver.catan.common.game.board.buildings.City;
import com.gregswebserver.catan.common.game.board.buildings.Settlement;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.common.game.board.paths.Road;
import com.gregswebserver.catan.common.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.common.game.board.tiles.Tile;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.gameplay.DiceRoll;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.game.player.Player;
import com.gregswebserver.catan.common.network.Identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of catan that contains the game board, game state, and player information.
 */
public class CatanGame implements EventConsumer<GameEvent> {

    private GameBoard board;
    private List<GameEvent> history;
    private HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;
    private HashMap<Identity, Player> players;
    private Coordinate robberLocation;

    public CatanGame(GameType type) {
        //Create a new board
        board = type.generate();
        history = new LinkedList<>();
    }

    public boolean test(GameEvent event) {
        switch (event.getType()) {
            //TODO: turn checks
            case Build_Settlement:
                return board.hexArray.vertices.get((Coordinate) event.getPayload()) == null;
            case Build_City:
                Building b = board.hexArray.vertices.get((Coordinate) event.getPayload());
                return (b instanceof Settlement && b.getOwner().getIdentity().equals(event.getOrigin()));
            case Build_Road:
                return board.hexArray.edges.get((Coordinate) event.getPayload()) == null;
            case Player_Select_Location:
                return true;
            case Player_Move_Robber:
                Tile tile = board.hexArray.spaces.get((Coordinate) event.getPayload());
                return (tile instanceof ResourceTile);
            case Player_Roll_Dice:
                //TODO: turn checks
                return true;
            case Trade_Bank:
                //TODO: trading
                break;
            case Trade_Offer:
                break;
            case Trade_Accept:
                break;
        }
        return false;
    }

    public void execute(GameEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        history.add(event);
        Identity origin = event.getOrigin();
        Player player = players.get(origin);
        Coordinate coordinate;
        switch (event.getType()) {
            case Build_Settlement:
                coordinate = (Coordinate) event.getPayload();
                Settlement settlement = new Settlement(player);
                board.hexArray.place(coordinate, settlement);
                break;
            case Build_City:
                coordinate = (Coordinate) event.getPayload();
                City city = new City(player);
                board.hexArray.place(coordinate, city);
                break;
            case Build_Road:
                coordinate = (Coordinate) event.getPayload();
                Road road = new Road(player);
                board.hexArray.place(coordinate, road);
                break;
            case Trade_Bank:
                break;
            case Trade_Offer:
                break;
            case Trade_Accept:
                break;
            case Player_Select_Location:
                coordinate = (Coordinate) event.getPayload();
                player.setSelected(coordinate);
                break;
            case Player_Move_Robber:
                coordinate = (Coordinate) event.getPayload();
                ResourceTile newTile = (ResourceTile) board.hexArray.spaces.get(coordinate);
                ResourceTile oldTile = (ResourceTile) board.hexArray.spaces.get(robberLocation);
                oldTile.removeRobber();
                newTile.placeRobber();
                robberLocation = coordinate;
                break;
            case Player_Roll_Dice:
                break;
            case Player_Turn_Advance:
                break;
        }
    }

    public GameBoard getBoard() {
        return board;
    }
}
