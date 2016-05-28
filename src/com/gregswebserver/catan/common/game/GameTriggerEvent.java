package com.gregswebserver.catan.common.game;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by greg on 5/28/16.
 * An event regarding the internal behavior of a CatanGame.
 */
public abstract class GameTriggerEvent<O,T extends EventType> extends InternalEvent<O,T> {

    protected GameTriggerEvent(O origin, T type, Object payload) {
        super(origin, type, payload);
    }
}
