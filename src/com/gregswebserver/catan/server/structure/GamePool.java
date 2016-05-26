package com.gregswebserver.catan.server.structure;

import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.GameEvent;
import com.gregswebserver.catan.common.game.event.GameControlEvent;
import com.gregswebserver.catan.common.game.event.GameControlEventType;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.structure.game.GameProgress;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.server.Server;

import java.util.HashMap;

/**
 * Created by greg on 1/26/16.
 * A pool of games and game threads that are concurrently active on the server.
 */
public class GamePool {

    private final Server host;
    private final HashMap<Integer, GameThread> games;
    private int nextGameID;

    public GamePool(Server host) {
        this.host = host;
        this.games = new HashMap<>();
        this.nextGameID = 0;
    }

    private synchronized int getNextGameID() {
        return nextGameID++;
    }

    public void process(int gameID, GameEvent event) {
        games.get(gameID).addEvent(new GameControlEvent(this, GameControlEventType.Execute, event));
    }

    public int start(GameSettings settings) {
        int gameID = getNextGameID();
        GameThread thread = new GameThread(host.logger, settings){
            @Override
            public String toString() {
                return "ServerGameThread #"+gameID;
            }

            @Override
            protected void onSuccess(GameControlEvent event) {
            }
            @Override
            protected void onFailure(EventConsumerException e) {
                logger.log("Unable to execute game event!", e, LogLevel.ERROR);
            }
        };
        games.put(gameID, thread);
        return gameID;
    }

    public void finish(int gameID) {
        games.remove(gameID).stop();
    }

    public GameProgress getGameProgress(int gameID) {
        return games.get(gameID).getProgress();
    }

    public void join() {
        for (GameThread thread : games.values()) {
            if (thread.isRunning())
                thread.join();
        }
    }
}
