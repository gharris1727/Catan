package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.network.Identity;

/**
 * Created by Greg on 8/12/2014.
 * Event containing game modification and turn changes.
 */
public class GameEvent extends ExternalEvent {

    public GameEvent(Identity origin) {
        super(origin);
    }
}
