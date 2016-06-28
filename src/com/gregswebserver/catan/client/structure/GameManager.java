package com.gregswebserver.catan.client.structure;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.event.*;
import com.gregswebserver.catan.common.game.players.Player;
import com.gregswebserver.catan.common.game.test.EqualityException;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.structure.game.GameProgress;
import com.gregswebserver.catan.common.structure.game.GameSettings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by greg on 3/13/16.
 * Class that manages the local and remote games, and allows for lag-hiding and history previews.
 */
public class GameManager {

    //TODO: explore lag-hiding and local/remote consistency verification.

    private final Client host;
    private final GameThread local;
    private final GameThread remote;
    private final List<GameEvent> events;
    private int localhistoryIndex;

    public GameManager(Client host, GameProgress progress) {
        this.host = host;
        GameSettings settings = progress.getSettings();
        local = new GameThread(host.logger, settings){
            @Override
            public String toString() {
                return host + " GameManagerLocalThread";
            }

            @Override
            protected void onSuccess(GameControlEvent event) {
                switch (event.getType()) {
                    case Test:
                        host.localSuccess(event);
                        break;
                    case Execute:
                        synchronized (GameManager.this) {
                            localhistoryIndex++;
                        }
                        host.refreshScreen();
                        break;
                    case Undo:
                        synchronized (GameManager.this) {
                            localhistoryIndex--;
                        }
                        host.refreshScreen();
                        break;
                }
                try {
                    if (isLive())
                        getLocalGame().assertEquals(getRemoteGame());
                } catch (EqualityException eq) {
                    host.logger.log(event + " Consistency error", eq, LogLevel.ERROR);
                }
            }
            @Override
            protected void onFailure(EventConsumerException e) {
                host.localFailure(e);
                try {
                    if (isLive())
                        getLocalGame().assertEquals(getRemoteGame());
                } catch (EqualityException eq) {
                    host.logger.log(e + " Consistency error", eq, LogLevel.ERROR);
                }
            }

            @Override
            protected void onFinish(GameEvent event) {
            }
        };
        remote = new GameThread(host.logger, settings) {
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
                try {
                    if (isLive())
                        getLocalGame().assertEquals(getRemoteGame());
                } catch (EqualityException eq) {
                    host.logger.log(event + " Consistency error", eq, LogLevel.ERROR);
                }
            }
            @Override
            protected void onFailure(EventConsumerException e) {
                host.logger.log(host + " Remote failure", e, LogLevel.ERROR);
                try {
                    if (isLive())
                        getLocalGame().assertEquals(getRemoteGame());
                } catch (EqualityException eq) {
                    host.logger.log(e + "Consistency error", eq, LogLevel.ERROR);
                }
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

    public synchronized boolean isLive() {
        return localhistoryIndex == events.size() - 1;
    }

    public synchronized void jumpToEvent(int index) {
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
        remote.addEvent(new GameControlEvent(this, GameControlEventType.Execute,event));
    }

    public boolean test(GameEvent gameEvent) {
        if (isLive()) {
            try {
                local.getGame().test(gameEvent);
                return true;
            } catch (EventConsumerException ignored) {
            }
        }
        return false;
    }

    public Username getLocalUsername() {
        return host.getToken().username;
    }

    public Player getLocalPlayer() {
        return local.getGame().getPlayers().getPlayer(getLocalUsername());
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
