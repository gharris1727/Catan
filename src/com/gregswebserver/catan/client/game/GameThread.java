package com.gregswebserver.catan.client.game;

import com.gregswebserver.catan.event.EventQueueThread;
import com.gregswebserver.catan.log.Logger;

/**
 * Created by Greg on 8/12/2014.
 * Main game thread that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 */
public class GameThread extends EventQueueThread<GameEvent> {

    public GameThread(Logger logger) {
        super(logger);
    }

    protected void execute() {
        GameEvent event = getEvent(true);
    }
}
