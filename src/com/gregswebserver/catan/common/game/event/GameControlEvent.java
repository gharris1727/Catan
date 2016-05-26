package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by greg on 3/13/16.
 * A control event that is specifically addressed at the GameThread.
 */
public class GameControlEvent extends InternalEvent<Object, GameControlEventType> {

    public GameControlEvent(Object origin, GameControlEventType type, Object payload) {
        super(origin, type, payload);
    }
}
