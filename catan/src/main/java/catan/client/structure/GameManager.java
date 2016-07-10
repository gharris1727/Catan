package catan.client.structure;

import catan.client.GameManagerListener;
import catan.common.IllegalStateException;
import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.CatanGame;
import catan.common.game.event.*;
import catan.common.game.players.Player;
import catan.common.log.Logger;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 3/13/16.
 * Class that manages the local and remote games, and allows for lag-hiding and history previews.
 */
public class GameManager {

    //TODO: explore lag-hiding and local/remote consistency verification.

    private final GameManagerListener host;
    private final Username username;
    private final GameThread local;
    private final GameThread remote;
    private final List<GameEvent> events;
    private int localhistoryIndex;

    public GameManager(GameManagerListener host, Logger logger, Username username, GameProgress progress) {
        this.host = host;
        this.username = username;
        GameSettings settings = progress.getSettings();
        local = new GameThread(logger, settings){
            @Override
            public String toString() {
                return host + " GameManagerLocalThread";
            }

            @Override
            protected void onSuccess(GameControlEvent event) {
                switch (event.getType()) {
                    case Test:
                        break;
                    case Execute:
                        localhistoryIndex++;
                        break;
                    case Undo:
                        localhistoryIndex--;
                        break;
                }
                host.localSuccess(event);
            }

            @Override
            protected void onFailure(EventConsumerException e) {
                host.localFailure(e);
            }

            @Override
            protected void onFinish(GameEvent event) {
            }
        };
        remote = new GameThread(logger, settings) {
            @Override
            public String toString() {
                return host + " GameManagerRemoteThread";
            }

            @Override
            protected void onSuccess(GameControlEvent event) {
                //A remote event was successful, so we should track it, as this is the true history of the game.
                switch (event.getType()) {
                    case Test:
                    case Undo:
                        throw new IllegalStateException();
                    case Execute:
                        if (isLive())
                            local.addEvent(event);
                        events.add((GameEvent) event.getPayload());
                        break;
                }
                host.remoteSuccess(event);
            }

            @Override
            protected void onFailure(EventConsumerException e) {
                host.remoteFailure(e);
            }

            @Override
            protected void onFinish(GameEvent event) {
                remote.stop();
            }
        };
        events = new ArrayList<>();
        events.add(new GameEvent(null, GameEventType.Start, null));
        List<GameEvent> history = progress.getHistory();
        for (int i = 1; i < history.size(); i++)
            remote(history.get(i));
        localhistoryIndex = 0;
    }

    public boolean isLive() {
        return localhistoryIndex == events.size() - 1;
    }

    public void jumpToEvent(int index) {
        if (index < 0 || index > events.size())
            index = events.size() - 1;
        if (localhistoryIndex < index)
            for (int i = localhistoryIndex + 1; i <= index; i++)
                local.addEvent(new GameControlEvent(this, GameControlEventType.Execute, events.get(i)));
        else
            for (int i = localhistoryIndex; i > index; i--)
                local.addEvent(new GameControlEvent(this, GameControlEventType.Undo, events.get(i)));
    }

    public void local(GameEvent gameEvent) {
        if (isLive())
            local.addEvent(new GameControlEvent(this, GameControlEventType.Test, gameEvent));
        else
            host.localFailure(new EventConsumerException("Game view is not live.", gameEvent));
    }

    public void remote(GameEvent event) {
        remote.addEvent(new GameControlEvent(this, GameControlEventType.Execute, event));
    }

    public boolean test(GameEvent gameEvent) {
        return isLive() && local.getGame().test(gameEvent) == null;
    }

    public Username getLocalUsername() {
        return username;
    }

    public Player getLocalPlayer() {
        return local.getGame().getPlayer(getLocalUsername());
    }

    public CatanGame getLocalGame() {
        return local.getGame();
    }

    public CatanGame getRemoteGame() {
        return remote.getGame();
    }

    public void join() {
        if (remote.isRunning())
            remote.stop();
        remote.join();
        if (local.isRunning())
            local.stop();
        local.join();
    }

    @Override
    public String toString() {
        return "GameManager";
    }
}
