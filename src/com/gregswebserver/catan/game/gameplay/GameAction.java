package com.gregswebserver.catan.game.gameplay;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 8/9/2014.
 * A generic action taken in the game, stored in a way that it can be undone.
 */
public class GameAction extends ExternalEvent {

    public final GameActionType type;
    public final Serializable data;

    public GameAction(Identity origin, GameActionType type, Serializable data) {
        super(origin);
        this.type = type;
        this.data = data;
    }
}
