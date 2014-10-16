package com.gregswebserver.catan.game;

import com.gregswebserver.catan.client.game.GameEvent;
import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.buildings.City;
import com.gregswebserver.catan.game.board.buildings.Settlement;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.paths.Road;
import com.gregswebserver.catan.game.board.tiles.ResourceTile;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.DiceRoll;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.game.player.Player;
import com.gregswebserver.catan.network.Identity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of catan that contains the game board, game state, and player information.
 */
public class CatanGame {

    //TODO: implement me

    private GameBoard board;
    private ArrayList<GameEvent> history;
    private HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;
    private HashMap<Identity, Player> players;

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

    private void moveRobber(Coordinate data) {
        Tile robbed = board.hexArray.spaces.get(data);
        if (robbed instanceof ResourceTile) {
            for (Tile tile : board.hexArray.spaces) {
                if (tile != null && tile instanceof ResourceTile) {
                    ResourceTile rTile = (ResourceTile) tile;
                    if (rTile.hasRobber())
                        rTile.removeRobber();
                    break;
                }
            }
            ((ResourceTile) robbed).placeRobber();
        }
    }

    private void roll(DiceRoll data) {
    }

    private void selectLocation(Coordinate data) {
    }

    public void process(GameEvent event) {
        history.add(event);
        switch (event.type) {
            case Game_Join:
                addPlayer((Identity) event.data);
                break;
            case Game_Leave:
                removePlayer((Identity) event.data);
                break;
            case Build_Settlement:
                buildSettlement(event.origin, (Coordinate) event.data);
                break;
            case Build_City:
                buildCity(event.origin, (Coordinate) event.data);
                break;
            case Build_Road:
                buildRoad(event.origin, (Coordinate) event.data);
                break;
            case Player_Select_Location:
                selectLocation((Coordinate) event.data);
                break;
            case Player_Move_Robber:
                moveRobber((Coordinate) event.data);
                break;
            case Player_Roll_Dice:
                roll((DiceRoll) event.data);
                break;
            case Trade_Bank:
                break;
            case Trade_Offer:
                break;
            case Trade_Accept:
                break;
        }
    }

    public GameBoard getBoard() {
        return board;
    }
}
