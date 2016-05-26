package com.gregswebserver.catan.common.game.teams;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

/**
 * Created by greg on 5/25/16.
 * Type of event applied to a team.
 */
public enum TeamEventType implements EventType {

    Use_Robber(Coordinate.class),
    Build_First_Outpost(Coordinate.class),
    Build_Second_Outpost(Coordinate.class),
    Build_Free_Road(Coordinate.class),
    Finish_Setup_Turn(null),
    Finish_Turn(null);

    private final Class payloadType;

    TeamEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
