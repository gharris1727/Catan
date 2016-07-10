package catan.common.game.teams;

import catan.common.game.event.GameTriggerEvent;

/**
 * Created by greg on 5/25/16.
 * An event that controls the behavior of a whole team.
 */
public class TeamEvent extends GameTriggerEvent<TeamColor, TeamEventType> {
    public TeamEvent(TeamColor origin, TeamEventType type, Object payload) {
        super(origin, type, payload);
    }
}
