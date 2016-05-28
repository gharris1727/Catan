package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.structure.game.GameSettings;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

/**
 * Created by greg on 2/5/16.
 * A configuration of users and teams that are participating in a catan game.
 */
public class PlayerPool implements ReversibleEventConsumer<PlayerEvent>, Iterable<Username> {

    private final Map<Username, Player> players;
    private final Stack<PlayerEvent> history;

    public PlayerPool(GameSettings settings) {
        players = new HashMap<>();
        for (Map.Entry<Username, TeamColor> entry : settings.playerTeams.entrySet())
            players.put(entry.getKey(), new Player(entry.getKey(), entry.getValue()));
        history = new Stack<>();
    }

    @Override
    public Iterator<Username> iterator() {
        return players.keySet().iterator();
    }

    public Player getPlayer(Username origin) {
        return players.get(origin);
    }

    @Override
    public String toString() {
        return "PlayerPool(" + players + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerPool that = (PlayerPool) o;

        if (!players.equals(that.players)) return false;
        return history.equals(that.history);

    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        PlayerEvent event = history.pop();
        try {
            getPlayer(event.getOrigin()).undo();
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(PlayerEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        Player player = getPlayer(event.getOrigin());
        if (player == null)
            throw new EventConsumerException("No player");
        player.test(event);
    }

    @Override
    public void execute(PlayerEvent event) throws EventConsumerException {
        test(event);
        try {
            history.push(event);
            getPlayer(event.getOrigin()).execute(event);
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }
}
