package catan.common.game.event;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.QueuedInputThread;
import catan.common.game.CatanGame;
import catan.common.game.teams.TeamColor;
import catan.common.log.Logger;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greg on 8/12/2014.
 * catan.Main game event that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
 * Simulates a catan game to store state and condition.
 */
public abstract class GameThread extends QueuedInputThread<GameControlEvent> {

    private final GameSettings settings;
    private final CatanGame game;

    protected GameThread(Logger logger, GameSettings settings) {
        super(logger);
        this.settings = settings;
        this.game = new CatanGame(settings);
        start();
    }

    public CatanGame getGame() {
        return game;
    }

    public GameProgress getProgress() {
        List<GameEvent> events = new ArrayList<>(game.getHistory().size());
        for (GameHistory h : game.getHistory())
            events.add(h.getGameEvent());
        return new GameProgress(settings, events);
    }

    //Process GameEvents from the event queue.
    @Override
    protected void execute() throws ThreadStop {
        GameControlEvent event = getEvent(true);
        //logger.log(this + " Received " + event, LogLevel.DEBUG);
        try {
            GameEvent gameEvent = (GameEvent) event.getPayload();
            synchronized(game) {
                switch (event.getType()) {
                    case Test:
                        EventConsumerProblem problem = game.test(gameEvent);
                        if (problem != null)
                            throw new EventConsumerException(event + " " + problem.getMessage());
                        break;
                    case Execute:
                        game.execute(gameEvent);
                        break;
                    case Undo:
                        game.undo();
                        break;
                }
            }
            onSuccess(event);
            if (game.getWinner() != TeamColor.None)
                onFinish(gameEvent);
        } catch (EventConsumerException e) {
            onFailure(e);
        }
    }

    protected abstract void onSuccess(GameControlEvent event);

    protected abstract void onFailure(EventConsumerException e);

    protected abstract void onFinish(GameEvent event);

}
