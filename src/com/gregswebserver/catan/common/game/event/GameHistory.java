package com.gregswebserver.catan.common.game.event;

import java.util.List;

/**
 * Created by greg on 5/25/16.
 * A historical step in the history of a CatanGame.
 */
public class GameHistory {

    private final GameEvent gameEvent;
    private final List<GameTriggerEvent> triggeredEvents;

    public GameHistory(GameEvent gameEvent, List<GameTriggerEvent> triggeredEvents) {
        this.gameEvent = gameEvent;
        this.triggeredEvents = triggeredEvents;
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public List<GameTriggerEvent> getTriggeredEvents() {
        return triggeredEvents;
    }
}
