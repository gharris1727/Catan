package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.CoreThread;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.GameSettings;
import com.gregswebserver.catan.common.log.LogLevel;

/**
 * Created by Greg on 8/12/2014.
 * Main game event that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public class GameThread extends QueuedInputThread<GameEvent> {

    private final CoreThread host;
    private final CatanGame game;

    public GameThread(CoreThread host, GameSettings settings) {
        super(host.logger);
        this.host = host;
        logger.log("Creating new GameThread for " + settings, LogLevel.DEBUG);
        this.game = new CatanGame(settings);
        start();
    }

    public CatanGame getGame() {
        return game;
    }

    //Process GameEvents from the event queue.
    @Override
    protected void execute() throws ThreadStop {
        GameEvent event = getEvent(true);
        try {
            game.execute(event);
        } catch (EventConsumerException e) {
            logger.log(e, LogLevel.ERROR);
        }
    }

    public String toString() {
        return host + " GameThread";
    }

}
