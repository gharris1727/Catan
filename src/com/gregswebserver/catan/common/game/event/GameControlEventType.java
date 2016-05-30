package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by greg on 3/13/16.
 * A control event pertaining to a game, but not the game itself.
 */
public enum GameControlEventType implements EventType {

    Test(GameEvent.class),
    Execute(GameEvent.class),
    Undo(GameEvent.class);

    private final Class payloadType;

    GameControlEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }

}
