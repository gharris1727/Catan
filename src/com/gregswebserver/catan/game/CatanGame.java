package com.gregswebserver.catan.game;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
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
    }

    public void roll(Identity origin, DiceRoll data) {

    }
}
