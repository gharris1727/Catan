package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;

/**
 * Created by Greg on 8/12/2014.
 * Main game event that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public class GameThread extends QueuedInputThread<GameEvent> {

    private QueuedInputThread<GenericEvent> host;
    private CatanGame game;

    public GameThread(QueuedInputThread<GenericEvent> host) {
        super(host.logger);
        this.host = host;
    }

    //Process GameEvents from the event queue.
    @Override
    protected void execute() throws ThreadStop {
        GameEvent event = getEvent(true);
    }

    public String toString() {
        return host + "GameThread";
    }
}
