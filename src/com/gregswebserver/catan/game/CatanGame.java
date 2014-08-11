package com.gregswebserver.catan.game;

import com.gregswebserver.catan.game.board.GameBoard;
import com.gregswebserver.catan.game.gameplay.GameAction;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.game.player.Player;

import java.util.ArrayList;

/**
 * Created by Greg on 8/8/2014.
 * Main class for a game of catan that contains the game board, game state, and player information.
 */
public class CatanGame {

    private GameBoard board;
    private ArrayList<GameAction> history;
    private ArrayList<Player> players;

    public CatanGame(GameType type) {
        type.LoadSettingsTo(board);
    }
}
