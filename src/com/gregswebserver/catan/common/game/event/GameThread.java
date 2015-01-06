package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.client.event.RenderEvent;
import com.gregswebserver.catan.client.event.RenderEventType;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.GenericEvent;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.event.ThreadStop;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.log.LogLevel;

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
    protected void execute() throws ThreadStop {
        GameEvent event = getEvent(true);
        try {
            game.execute(event);
        } catch (EventConsumerException e) {
            logger.log("Error while applying GameEvent", e, LogLevel.ERROR);
        }
        host.addEvent(new RenderEvent(this, RenderEventType.Game_Update, null));
    }

    public void createNew(GameType type) {
        game = new CatanGame(type);
        host.addEvent(new RenderEvent(this, RenderEventType.Game_Create, game));
    }

    public String toString() {
        return host + "GameThread";
    }
}
