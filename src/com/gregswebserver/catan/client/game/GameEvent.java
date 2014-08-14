package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.network.Identity;

/**
 * Created by Greg on 8/12/2014.
 * Created when a player makes a move in a Catan Game.
 * Sent to the server, where it simulates a game.
 * Rebroadcast out to the other clients to simulate their local games.
 * Terminates in a GameEvent thread that processes it.
 */
public class GameEvent extends ExternalEvent {

    public final GameEventType type;
    public final Object data;

    public GameEvent(Identity origin, GameEventType type, Object data) {
        super(origin);
        this.type = type;
        this.data = data;
    }
}
