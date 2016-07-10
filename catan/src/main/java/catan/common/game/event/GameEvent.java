package catan.common.game.event;

import catan.common.crypto.Username;
import catan.common.event.ExternalEvent;

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

}
