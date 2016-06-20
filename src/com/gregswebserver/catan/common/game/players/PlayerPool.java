package com.gregswebserver.catan.common.game.players;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.teams.TeamColor;
import com.gregswebserver.catan.common.game.test.AssertEqualsTestable;
import com.gregswebserver.catan.common.game.test.EqualityException;
import com.gregswebserver.catan.common.structure.game.GameSettings;

import java.util.*;

/**
 * Created by greg on 2/5/16.
 * A configuration of users and teams that are participating in a catan game.
 */
public class PlayerPool implements ReversibleEventConsumer<PlayerEvent>, Iterable<Username>, AssertEqualsTestable<PlayerPool> {

    private final Map<Username, Player> players;
    private final Stack<Set<Username>> discards;
    private final Stack<PlayerEvent> history;

    public PlayerPool(GameSettings settings) {
        players = new HashMap<>();
        for (Map.Entry<Username, TeamColor> entry : settings.playerTeams.entrySet())
            players.put(entry.getKey(), new Player(entry.getKey(), entry.getValue()));
        discards = new Stack<>();
        discards.push(new HashSet<>());
        history = new Stack<>();
    }

    @Override
    public Iterator<Username> iterator() {
        return players.keySet().iterator();
    }

    public Player getPlayer(Username origin) {
        return players.get(origin);
    }

    public boolean hasDiscarded(Username origin) {
        return discards.peek().contains(origin);
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
            if (event.getType() == PlayerEventType.Finish_Discarding) {
                discards.pop();
            } else {
                if (event.getType() == PlayerEventType.Discard_Resources)
                    discards.peek().remove(event.getOrigin());
                getPlayer(event.getOrigin()).undo();
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(PlayerEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        if (event.getType() == PlayerEventType.Finish_Discarding) {
            for (Username username : this) {
                Player p = players.get(username);
                if (p.getDiscardCount() > 0 && !discards.peek().contains(username))
                    throw new EventConsumerException(username + " has not finished discarding");
            }
        } else {
            Player player = getPlayer(event.getOrigin());
            if (player == null)
                throw new EventConsumerException("No player");
            if (event.getType() == PlayerEventType.Discard_Resources && discards.peek().contains(event.getOrigin()))
                throw new EventConsumerException("Player already discarded");
            player.test(event);
        }
    }

    @Override
    public void execute(PlayerEvent event) throws EventConsumerException {
        test(event);
        try {
            history.push(event);
            if (event.getType() == PlayerEventType.Finish_Discarding) {
                discards.push(new HashSet<>());
            } else {
                if (event.getType() == PlayerEventType.Discard_Resources)
                    discards.peek().add(event.getOrigin());
                getPlayer(event.getOrigin()).execute(event);
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void assertEquals(PlayerPool other) throws EqualityException {
        if (this == other)
            return;

        for (Username u : this)
            players.get(u).assertEquals(other.players.get(u));
        if (!discards.equals(other.discards))
            throw new EqualityException("PlaterPoolDiscards", discards, other.discards);
        if (!history.equals(other.history))
            throw new EqualityException("PlayerPoolHistory", history, other.history);
    }
}
