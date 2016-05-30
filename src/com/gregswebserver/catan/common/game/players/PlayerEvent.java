package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.event.GameTriggerEvent;

/**
 * Created by greg on 5/24/16.
 * An event that can be applied to a player.
 */
public class PlayerEvent extends GameTriggerEvent<Username, PlayerEventType> {

    public PlayerEvent(Username origin, PlayerEventType type, Object payload) {
        super(origin, type, payload);
    }
}
