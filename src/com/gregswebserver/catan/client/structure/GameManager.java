package com.gregswebserver.catan.client.structure;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.game.event.*;
import com.gregswebserver.catan.common.log.LogLevel;
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

    public GameManager(Client host, GameSettings settings) {
        this.host = host;
        local = new GameThread(host.logger, settings){
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
                        host.gameUpdate();
                        break;
                    case Undo:
                        synchronized (GameManager.this) {
                            localhistoryIndex--;
                        }
                        host.gameUpdate();
                        break;
                }
            }
            @Override
            protected void onFailure(EventConsumerException e) {
                host.localFailure(e);
            }
        };
        remote = new GameThread(host.logger, settings) {
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
            }
            @Override
            protected void onFailure(EventConsumerException e) {
                host.logger.log("Unable to perform remote change:", e, LogLevel.WARN);
            }
        };
        events = new ArrayList<>();
        events.add(new GameEvent(null, GameEventType.Start, null));
        localhistoryIndex = 0;
    }

    private synchronized boolean isLive() {
        return localhistoryIndex == events.size() - 1;
    }

    public synchronized void jumpToEvent(int index) {
        if (index < 0 || index > events.size())
            return;
        if (localhistoryIndex < index)
            for (int i = localhistoryIndex + 1; i <= index; i++)
                local.addEvent(new GameControlEvent(this, GameControlEventType.Execute, events.get(i)));
        else
            for (int i = localhistoryIndex; i > index; i--)
                local.addEvent(new GameControlEvent(this, GameControlEventType.Undo, events.get(i)));

    }

    public void jumpToLive() {
        if (!isLive())
            jumpToEvent(events.size());
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

    public List<GameEvent> getEvents() {
        return events;
    }

    public CatanGame getLocalGame() {
        return local.getGame();
    }

    public void stop() {
        local.stop();
        remote.stop();
    }
}
