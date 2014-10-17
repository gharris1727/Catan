package com.gregswebserver.catan.common.game;

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

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of catan that contains the game board, game state, and player information.
 */
public class CatanGame {

    private GameBoard board;
    private ArrayList<GameEvent> history;
    private HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;
    private HashMap<Identity, Player> players;
    private HashMap<Identity, Coordinate> selected;
    private Coordinate robberLocation;

    public CatanGame(GameType type) {
        //Create a new board
        board = new GameBoard();
        type.generate(board);
    }

    private void addPlayer(Identity identity) {
        Player player = new Player(identity);
        players.put(identity, player);
    }

    private void removePlayer(Identity origin) {
        players.remove(origin);
    }

    private void buildSettlement(Identity origin, Coordinate coordinate) {
        Settlement settlement = new Settlement(players.get(origin));
        board.hexArray.place(coordinate, settlement);
    }

    private void buildCity(Identity origin, Coordinate coordinate) {
        City city = new City(players.get(origin));
        board.hexArray.place(coordinate, city);
    }

    private void buildRoad(Identity origin, Coordinate coordinate) {
        Road road = new Road(players.get(origin));
        board.hexArray.place(coordinate, road);
    }

    private void moveRobber(Coordinate coordinate) {
        ResourceTile newTile = (ResourceTile) board.hexArray.spaces.get(coordinate);
        ResourceTile oldTile = (ResourceTile) board.hexArray.spaces.get(robberLocation);
        oldTile.removeRobber();
        newTile.placeRobber();
        robberLocation = coordinate;
    }

    private void roll(DiceRoll diceRoll) {
    }

    private void selectLocation(Identity identity, Coordinate coordinate) {
        selected.put(identity, coordinate);
    }

    public void process(GameEvent event) {
        history.add(event);
        switch (event.getType()) {
            case Build_Settlement:
                buildSettlement(event.getOrigin(), (Coordinate) event.getPayload());
                break;
            case Build_City:
                buildCity(event.getOrigin(), (Coordinate) event.getPayload());
                break;
            case Build_Road:
                buildRoad(event.getOrigin(), (Coordinate) event.getPayload());
                break;
            case Player_Select_Location:
                selectLocation(event.getOrigin(), (Coordinate) event.getPayload());
                break;
            case Player_Move_Robber:
                moveRobber((Coordinate) event.getPayload());
                break;
            case Player_Roll_Dice:
                roll((DiceRoll) event.getPayload());
                break;
            case Trade_Bank:
                break;
            case Trade_Offer:
                break;
            case Trade_Accept:
                break;
        }
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

    public GameBoard getBoard() {
        return board;
    }
}
