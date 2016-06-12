package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by greg on 5/25/16.
 * Types of events that can mutate the game state.
 */
public enum GameStateEventType implements EventType {

    Roll_Dice(DiceRoll.class),
    Draw_DevelopmentCard(DevelopmentCard.class),
    Advance_Turn(TeamColor.class),
    Active_Turn(TeamColor.class),
    Advance_Theft(null);

    private final Class payloadType;

    GameStateEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
