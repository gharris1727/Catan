package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.event.QueuedInputThread;
import com.gregswebserver.catan.game.CatanGame;
import com.gregswebserver.catan.game.board.hexarray.Coordinate;
import com.gregswebserver.catan.game.gameplay.DiceRoll;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.log.Logger;

/**
 * Created by Greg on 8/12/2014.
 * Main game thread that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public class GameThread extends QueuedInputThread<GameEvent> {

    private CatanGame game;

    public GameThread(Logger logger) {
        super(logger);
    }

    //Process GameEvents from the event queue.
    protected void execute() {
        GameEvent event = getEvent(true);
        switch (event.type) {
            case Game_Create:
                game = new CatanGame((GameType) event.data);
                break;
            case Player_Join:
                game.addPlayer(event.origin);
                break;
            case Player_Leave:
                game.removePlayer(event.origin);
                break;
            case Player_Build_Settlement:
                game.buildSettlement(event.origin, (Coordinate) event.data);
                break;
            case Player_Build_City:
                game.buildCity(event.origin, (Coordinate) event.data);
                break;
            case Player_Build_Road:
                game.buildRoad(event.origin, (Coordinate) event.data);
                break;
            case Player_Move_Robber:
                game.moveRobber(event.origin, (Coordinate) event.data);
                break;
            case Player_Roll_Dice:
                game.roll(event.origin, (DiceRoll) event.data);
                break;
            case Player_Offer_Trade:
                break;
            case Player_Accept_Trade:
                break;
            case Player_Make_Trade:
                break;
            //TODO: implement trading
        }
    }
}
