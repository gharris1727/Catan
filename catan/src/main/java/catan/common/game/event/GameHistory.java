package catan.common.game.event;

import catan.common.game.teams.TeamColor;

import java.util.Collections;
import java.util.List;

/**
 * Created by greg on 5/25/16.
 * A historical step in the history of a CatanGame.
 */
public class GameHistory {

    private final GameEvent gameEvent;
    private final TeamColor team;
    private final List<GameTriggerEvent> triggeredEvents;

    public GameHistory(GameEvent gameEvent, TeamColor team, List<GameTriggerEvent> triggeredEvents) {
        this.gameEvent = gameEvent;
        this.team = team;
        this.triggeredEvents = Collections.unmodifiableList(triggeredEvents);
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public TeamColor getTeam() {
        return team;
    }

    public List<GameTriggerEvent> getTriggeredEvents() {
        return triggeredEvents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        GameHistory other = (GameHistory) o;

        if (!gameEvent.equals(other.gameEvent)) return false;
        return triggeredEvents.equals(other.triggeredEvents);

    }

    @Override
    public int hashCode() {
        int result = gameEvent.hashCode();
        result = (31 * result) + triggeredEvents.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "GameHistory{" +
            "gameEvent=" + gameEvent +
            ", triggeredEvents=" + triggeredEvents +
            '}';
    }
}
