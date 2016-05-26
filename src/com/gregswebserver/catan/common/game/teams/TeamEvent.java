package com.gregswebserver.catan.common.game.teams;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by greg on 5/25/16.
 * An event that controls the behavior of a whole team.
 */
public class TeamEvent extends InternalEvent<TeamColor, TeamEventType> {
    public TeamEvent(TeamColor origin, TeamEventType type, Object payload) {
        super(origin, type, payload);
    }
}
