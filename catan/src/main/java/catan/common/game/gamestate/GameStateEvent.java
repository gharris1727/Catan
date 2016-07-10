package catan.common.game.gamestate;

import catan.common.crypto.Username;
import catan.common.game.event.GameTriggerEvent;

/**
 * Created by greg on 5/25/16.
 * An event that can be applied to the game state management objects.
 */
public class GameStateEvent extends GameTriggerEvent<Username, GameStateEventType> {

    public GameStateEvent(Username origin, GameStateEventType type, Object payload) {
        super(origin, type, payload);
    }
}
