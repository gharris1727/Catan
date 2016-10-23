package catan.server.structure;

import catan.common.event.EventConsumerException;
import catan.common.game.event.GameControlEvent;
import catan.common.game.event.GameControlEventType;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameThread;
import catan.common.game.gameplay.allocator.TeamAllocation;
import catan.common.log.LogLevel;
import catan.common.structure.event.LobbyEvent;
import catan.common.structure.event.LobbyEventType;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;
import catan.server.Server;

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
                host.broadcastGameEvent((GameEvent) event.getPayload());
            }
            @Override
            protected void onFailure(EventConsumerException e) {
                logger.log("Server unable to execute event!", e, LogLevel.ERROR);
            }

            @Override
            protected void onFinish(GameEvent e) {
                host.addEvent(new LobbyEvent(e.getOrigin(), LobbyEventType.Game_Finish, null));
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

    public TeamAllocation getGamePlayers(int gameID) {
        return games.get(gameID).getGame().getTeams();
    }

    public void join() {
        for (GameThread thread : games.values()) {
            if (thread.isRunning())
                thread.join();
        }
    }
}