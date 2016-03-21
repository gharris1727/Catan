package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.ExternalEvent;

/**
 * Created by Greg on 8/12/2014.
 * Created when a player makes a move in a Catan Game.
 * Sent to the server, where it simulates a game.
 * Rebroadcast out to the other clients to simulate their local config.games.
 * Terminates in a GameEvent event that processes it.
 */
public class GameEvent extends ExternalEvent<GameEventType> {

    public GameEvent(Username origin, GameEventType type, Object payload) {
        super(origin, type, payload);
    }

    public String getDescription() {
        switch (getType() ) {
            case Start:
                return "Start Game";
            case Turn_Advance:
                return "End Turn";
            case Player_Move_Robber:
                return "Move Robber";
            case Build_Settlement:
                return "Build Settlement";
            case Build_City:
                return "Build City";
            case Build_Road:
                return "Build Road";
            case Buy_Development:
                return "Buy Development Card";
            case Offer_Trade:
                return "Propose Trade";
            case Make_Trade:
                return "Complete Trade";
        }
        return "NO DESCRIPTION";
    }
}
