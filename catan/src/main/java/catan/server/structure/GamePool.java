package catan.server.structure;

import catan.common.event.EventConsumerException;
import catan.common.event.QueuedInputThread;
import catan.common.game.CatanGame;
import catan.common.game.event.GameEvent;
import catan.common.game.event.GameHistory;
import catan.common.game.gameplay.allocator.TeamAllocation;
import catan.common.game.teams.TeamColor;
import catan.common.log.LogLevel;
import catan.common.structure.event.LobbyEvent;
import catan.common.structure.event.LobbyEventType;
import catan.common.structure.game.GameProgress;
import catan.common.structure.game.GameSettings;
import catan.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public synchronized void process(int gameID, GameEvent event) {
        games.get(gameID).addEvent(event);
    }

    public synchronized int start(GameSettings settings) {
        GameThread thread = new GameThread(settings);
        games.put(thread.gameId, thread);
        return thread.gameId;
    }

    public synchronized void finish(int gameID) {
        games.remove(gameID).stop();
    }

    public synchronized GameProgress getGameProgress(int gameID) {
        return games.get(gameID).getProgress();
    }

    public synchronized TeamAllocation getGamePlayers(int gameID) {
        return games.get(gameID).getGame().getTeams();
    }

    public synchronized void join() {
        for (GameThread thread : games.values()) {
            if (thread.isRunning())
                thread.join();
        }
    }

    /**
     * Created by Greg on 8/12/2014.
     * catan.Main game event that consumes GameEvents from the blocking queue, and preforms the actions on the game element.
     * Simulates a catan game to store state and condition.
     */
    private class GameThread extends QueuedInputThread<GameEvent> {

        private final int gameId;
        private final GameSettings settings;
        private final CatanGame game;

        private GameThread(GameSettings settings) {
            super(host.logger);
            gameId = getNextGameID();
            this.settings = settings;
            this.game = new CatanGame(settings);
            start();
        }

        private CatanGame getGame() {
            return game;
        }

        private GameProgress getProgress() {
            List<GameEvent> events = new ArrayList<>(game.getHistory().size());
            for (GameHistory h : game.getHistory())
                events.add(h.getGameEvent());
            return new GameProgress(settings, events);
        }

        //Process GameEvents from the event queue.
        @Override
        protected void execute() throws ThreadStop {
            GameEvent event = getEvent(true);
            //logger.log(this + " Received " + event, LogLevel.DEBUG);
            if (game.test(event) == null) {
                try {
                    game.execute(event);
                    host.broadcastGameEvent(event);
                    if (game.getWinner() != TeamColor.None)
                        host.addEvent(new LobbyEvent(event.getOrigin(), LobbyEventType.Game_Finish, null));
                } catch (EventConsumerException e) {
                    logger.log("Server unable to execute event!", e, LogLevel.ERROR);
                }
            } else {
                logger.log("Server caught invalid event: " + event, LogLevel.WARN);
            }
        }

        @Override
        public String toString() {
            return "GameThread #"+gameId;
        }

    }
}
