package catan.common.structure.game;

import catan.common.event.EventPayload;
import catan.common.game.event.GameEvent;

import java.util.List;

/**
 * Created by greg on 4/1/16.
 * Class to package all details about a CatanGame to send to the client.
 */
public class GameProgress extends EventPayload {

    private final GameSettings settings;
    private final List<GameEvent> history;

    public GameProgress(GameSettings settings, List<GameEvent> history) {
        this.settings = settings;
        this.history = history;
    }

    public GameSettings getSettings() {
        return settings;
    }

    public List<GameEvent> getHistory() {
        return history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameProgress that = (GameProgress) o;

        if (settings != null ? !settings.equals(that.settings) : that.settings != null) return false;
        return history != null ? history.equals(that.history) : that.history == null;
    }

    @Override
    public int hashCode() {
        int result = settings != null ? settings.hashCode() : 0;
        result = 31 * result + (history != null ? history.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GameProgress(" + settings + "/" + (history == null ? 0 : history.size()) + ")";
    }
}
