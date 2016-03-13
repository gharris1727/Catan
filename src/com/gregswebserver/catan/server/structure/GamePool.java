package com.gregswebserver.catan.server.structure;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.structure.game.GameSettings;
import com.gregswebserver.catan.server.Server;

import java.util.HashMap;

/**
 * Created by greg on 1/26/16.
 * A pool of games and game threads that are concurrently active on the server.
 */
public class GamePool {

    private final Server host;
    private final HashMap<Username, GameThread> games;

    public GamePool(Server host) {
        this.host = host;
        this.games = new HashMap<>();
    }

    public void process(GameEvent event) {
        games.get(event.getOrigin()).addEvent(event);
    }

    public void start(GameSettings settings) {
        GameThread thread = new GameThread(host, host.getToken().username, settings);
        for (Username username : settings.playerTeams.keySet())
            games.put(username, thread);
    }

    public void finish(Username username) {
        games.put(username, null);
    }
}
