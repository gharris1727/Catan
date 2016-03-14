package com.gregswebserver.catan.common.game.event;

import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.QueuedInputThread;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.structure.game.GameSettings;

/**
 * Created by Greg on 8/12/2014.
 * Main game event that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public abstract class GameThread extends QueuedInputThread<GameControlEvent> {

    private final CatanGame game;

    public GameThread(Logger logger, GameSettings settings) {
        super(logger);
        this.game = new CatanGame(settings);
        start();
    }

    public CatanGame getGame() {
        return game;
    }

    //Process GameEvents from the event queue.
    @Override
    protected void execute() throws ThreadStop {
        GameControlEvent event = getEvent(true);
        try {
            GameEvent gameEvent = null;
            if (event.getPayload() instanceof GameEvent)
                gameEvent = (GameEvent) event.getPayload();
            switch (event.getType()){
                case Test:
                    if (!game.test(gameEvent))
                        throw new EventConsumerException("Game action not allowed", gameEvent);
                    break;
                case Execute:
                    game.execute(gameEvent);
                    break;
                case Undo:
                    game.undo(gameEvent);
                    break;
            }
            onSuccess(event);
        } catch (EventConsumerException e) {
            onFailure(event, e);
        }
    }

    protected abstract void onSuccess(GameControlEvent event);

    protected abstract void onFailure(GameControlEvent event, EventConsumerException e);

    public String toString() {
        return "GameThread";
    }

}
