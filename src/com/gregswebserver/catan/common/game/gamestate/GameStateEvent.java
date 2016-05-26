package com.gregswebserver.catan.common.game.gamestate;

import com.gregswebserver.catan.common.game.event.GameEffectEvent;

/**
 * Created by greg on 5/25/16.
 * An event that can be applied to the game state management objects.
 */
public class GameStateEvent extends GameEffectEvent<Object, GameStateEventType> {

    public GameStateEvent(Object origin, GameStateEventType type, Object payload) {
        super(origin, type, payload);
    }
}
