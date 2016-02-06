package com.gregswebserver.catan.server.structure;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumer;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.GameSettings;
import com.gregswebserver.catan.common.game.event.GameEvent;
import com.gregswebserver.catan.common.game.event.GameThread;
import com.gregswebserver.catan.common.structure.Lobby;
import com.gregswebserver.catan.server.Server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 1/26/16.
 * A pool of games and game threads that are concurrently active on the server.
 */
public class GamePool implements EventConsumer<GameEvent> {

    private final Server host;
    private final Map<Username, GameThread> games;

    public GamePool(Server host) {
        this.host = host;
        this.games = new HashMap<>();
    }

    @Override
    public boolean test(GameEvent event) {
        boolean inGame = games.containsKey(event.getOrigin());
        switch(event.getType()) {
            case Game_Create:
                return !inGame;
            case Turn_Advance:
            case Player_Roll_Dice:
            case Player_Move_Robber:
            case Player_Select_Location:
            case Build_Settlement:
            case Build_City:
            case Build_Road:
                return inGame;
        }
        return false;
    }

    @Override
    public void execute(GameEvent event) throws EventConsumerException {
        if (!test(event))
            throw new EventConsumerException(event);
        switch(event.getType()) {
            case Game_Create:
                Lobby lobby = host.getUserLobby(event.getOrigin());
                GameSettings settings = lobby.getGameSettings();
                games.put(event.getOrigin(),new GameThread(host, settings));
                break;
            case Turn_Advance:
            case Player_Roll_Dice:
            case Player_Move_Robber:
            case Player_Select_Location:
            case Build_Settlement:
            case Build_City:
            case Build_Road:
                games.get(event.getOrigin()).addEvent(event);
        }
    }
}
