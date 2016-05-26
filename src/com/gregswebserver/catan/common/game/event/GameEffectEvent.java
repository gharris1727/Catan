package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by greg on 5/25/16.
 * Superclass of events that affect the behavior of a CatanGame.
 */
public abstract class GameEffectEvent<O, T extends EventType> extends InternalEvent<O,T> {

    protected GameEffectEvent(O origin, T type, Object payload) {
        super(origin, type, payload);
    }
}
