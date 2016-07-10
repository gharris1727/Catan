package catan.common.game.players;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.gameplay.allocator.TeamAllocation;
import catan.common.game.teams.TeamColor;

import java.util.*;

/**
 * Created by greg on 2/5/16.
 * A configuration of users and teams that are participating in a catan game.
 */
public class PlayerPool implements ReversibleEventConsumer<PlayerEvent>, Iterable<Username> {

    final Map<Username, Player> players;
    final Stack<Set<Username>> discards;
    final Stack<PlayerEvent> history;

    public PlayerPool(TeamAllocation teamAllocation) {
        players = new HashMap<>();
        for (Map.Entry<Username, TeamColor> entry : teamAllocation.getPlayerTeams().entrySet())
            players.put(entry.getKey(), new HumanPlayer(entry.getKey(), entry.getValue()));
        players.put(null, new Bank());
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
    public EventConsumerProblem test(PlayerEvent event) {
        if (event == null)
            return new EventConsumerProblem("No event");
        if (event.getType() == PlayerEventType.Finish_Discarding) {
            for (Username username : this) {
                Player p = players.get(username);
                if (p.getDiscardCount() > 0 && !discards.peek().contains(username))
                    return new EventConsumerProblem(username + " has not finished discarding");
            }
            return null;
        } else {
            Player player = getPlayer(event.getOrigin());
            if (player == null)
                return new EventConsumerProblem("No player");
            if (event.getType() == PlayerEventType.Discard_Resources && discards.peek().contains(event.getOrigin()))
                return new EventConsumerProblem("Player already discarded");
            return player.test(event);
        }
    }

    @Override
    public void execute(PlayerEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(problem);
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

}
