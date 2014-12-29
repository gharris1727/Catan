package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Created when a player makes a move in a Catan Game.
 * Sent to the server, where it simulates a game.
 * Rebroadcast out to the other clients to simulate their local games.
 * Terminates in a GameEvent event that processes it.
 */
public class GameEvent extends ExternalEvent<GameEventType> {

    public GameEvent(Identity origin, GameEventType type, Serializable payload) {
        super(origin, type, payload);
    }
}