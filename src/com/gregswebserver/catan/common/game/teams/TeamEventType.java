package com.gregswebserver.catan.common.game.teams;

import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by greg on 5/25/16.
 * Type of event applied to a team.
 */
public enum TeamEventType implements EventType {

    Activate_Robber(null),
    Use_Robber(null),
    Steal_Resources(null),
    Build_First_Outpost(null),
    Build_Second_Outpost(null),
    Activate_RoadBuilding(null),
    Build_Free_Road(null),
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
