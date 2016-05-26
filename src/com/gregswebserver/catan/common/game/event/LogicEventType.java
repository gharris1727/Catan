package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.BoardEvent;
import com.gregswebserver.catan.common.game.gameplay.players.PlayerEvent;
import com.gregswebserver.catan.common.game.gameplay.players.TeamEvent;
import com.gregswebserver.catan.common.game.gamestate.GameStateEvent;

import java.util.List;

/**
 * Created by greg on 5/25/16.
 * A logic event that serves as a logical composition of different event types.
 */
public enum LogicEventType implements EventType {

    AND(List.class),
    OR(List.class),
    NOT(LogicEvent.class),
    NOP(null),
    Player_Event(PlayerEvent.class),
    Board_Event(BoardEvent.class),
    Team_Event(TeamEvent.class),
    GameState_Event(GameStateEvent.class);

    private final Class payloadType;

    LogicEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }


}
