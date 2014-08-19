package com.gregswebserver.catan.game;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.board.tiles.Tile;
import com.gregswebserver.catan.game.gameplay.DiceRoll;
import com.gregswebserver.catan.game.gameplay.GameAction;
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
    private ArrayList<GameAction> history;
    private HashMap<DiceRoll, ArrayList<Coordinate>> diceRollCoordinates;
    private HashMap<Identity, Player> players;

    public CatanGame(GameType type) {
        //Create a new board
        board = new GameBoard();
        type.LoadSettingsTo(board);
    }

    public void addPlayer(Identity identity) {
    }

    public void removePlayer(Identity origin) {
    }

    public void buildSettlement(Identity origin, Coordinate data) {
    }

    public void buildCity(Identity origin, Coordinate data) {
    }

    public void buildRoad(Identity origin, Coordinate data) {
    }

    public void moveRobber(Identity origin, Coordinate data) {
        for (Tile tile : board.hexArray.spaces) {
            if (tile.hasRobber()) {
                tile.removeRobber();
                break;
            }
        }
        board.hexArray.spaces.get(data).placeRobber();
    }

    public void roll(Identity origin, DiceRoll data) {

    }

    public void applyAction(GameAction action) {
        history.add(action);
        switch (action.type) {
            case Player_Build_Settlement:
                break;
            case Player_Build_City:
                break;
            case Player_Build_Road:
                break;
            case Player_Move_Robber:
                break;
            case Player_Roll_Dice:
                break;
            case Player_Accept_Trade:
                break;
            case Player_Make_Trade:
                break;
        }
    }

    public GameBoard getBoard() {
        return board;
    }
}
