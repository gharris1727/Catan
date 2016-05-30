package com.gregswebserver.catan.common.game.board;

import com.gregswebserver.catan.common.game.event.GameTriggerEvent;
import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by greg on 5/24/16.
 * An event that affects the game board itself.
 */
public class BoardEvent extends GameTriggerEvent<TeamColor, BoardEventType> {

    public BoardEvent(TeamColor origin, BoardEventType type, Object payload) {
        super(origin, type, payload);
    }
}
