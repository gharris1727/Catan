package catan.client.structure;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.CatanGame;
import catan.common.game.GameObserver;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameEventType;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 3/13/16.
 * Class that manages the local and remote games, and allows for lag-hiding and history previews.
 */
public final class GameManager {

    //TODO: explore lag-hiding and local/remote consistency verification.

    private final Username username;
    private final boolean localPlaying;
    private final CatanGame local;
    private final CatanGame remote;
    private final List<GameEvent> events;
    private int localhistoryIndex;

    public GameManager(Username username, GameProgress progress) throws EventConsumerException {
        this.username = username;
        GameSettings settings = progress.getSettings();
        local = new CatanGame(settings);
        localPlaying = local.getObserver().getPlayerObserver(username) != null;
        remote = new CatanGame(settings);
        events = new ArrayList<>();
        events.add(new GameEvent(null, GameEventType.Start, settings));
        localhistoryIndex = 0;
        List<GameEvent> history = progress.getHistory();
        for (int i = 1; i < history.size(); i++) {
            remote(history.get(i));
        }
    }

    public synchronized boolean isLive() {
        return localhistoryIndex == (events.size() - 1);
    }

    public synchronized void jumpToEvent(int index) throws EventConsumerException {
        if ((index < 0) || (index > events.size()))
            index = events.size() - 1;
        if (localhistoryIndex < index) {
            for (int i = localhistoryIndex + 1; i <= index; i++) {
                local.execute(events.get(i));
                localhistoryIndex++;
            }
        } else {
            for (int i = localhistoryIndex; i > index; i--) {
                local.undo();
                localhistoryIndex--;
            }
        }
    }

    public synchronized void remote(GameEvent event) throws EventConsumerException {
        remote.execute(event);
        if (isLive()) {
            local.execute(event);
            localhistoryIndex++;
        }
        events.add(event);
    }

    public synchronized boolean test(GameEvent gameEvent) {
        return isLive() && (local.test(gameEvent) == null);
    }

    public Username getLocalUsername() {
        return username;
    }

    public GameObserver getLocalGame() {
        return local.getObserver();
    }

    public GameObserver getRemoteGame() {
        return remote.getObserver();
    }

    public boolean isLocalPlaying() {
        return localPlaying;
    }

    @Override
    public String toString() {
        return "GameManager";
    }
}
