package com.gregswebserver.catan.common.game.gameplay.players;

import com.gregswebserver.catan.common.game.event.GameEffectEvent;
import com.gregswebserver.catan.common.game.gameplay.enums.TeamColor;

/**
 * Created by greg on 5/25/16.
 * An event that controls the behavior of a whole team.
 */
public class TeamEvent extends GameEffectEvent<TeamColor, TeamEventType> {
    public TeamEvent(TeamColor origin, TeamEventType type, Object payload) {
        super(origin, type, payload);
    }
}
