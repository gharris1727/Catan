package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by greg on 5/25/16.
 * Types of events that can mutate the game state.
 */
public enum GameStateEventType implements EventType {

    Roll_Dice(null),
    Draw_DevelopmentCard(null),
    Advance_Turn(null);

    private final Class payloadType;

    GameStateEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
