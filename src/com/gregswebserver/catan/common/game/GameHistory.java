package com.gregswebserver.catan.common.game;

/**
 * Created by greg on 5/25/16.
 * A historical step in the history of a CatanGame.
 */
public class GameHistory {

    private final GameEvent gameEvent;
    private final LogicEvent logicEvent;

    public GameHistory(GameEvent gameEvent, LogicEvent logicEvent) {
        this.gameEvent = gameEvent;
        this.logicEvent = logicEvent;
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public LogicEvent getLogicEvent() {
        return logicEvent;
    }
}
