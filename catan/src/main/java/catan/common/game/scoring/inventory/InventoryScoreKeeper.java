package catan.common.game.scoring.inventory;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.players.PlayerPool;
import catan.common.game.scoring.ScoreEvent;
import catan.common.game.scoring.ScoreKeeper;
import catan.common.game.scoring.reporting.player.PlayerScoreReport;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.scoring.reporting.scores.SimpleScoreReport;
import catan.common.game.scoring.rules.GameRules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Created by greg on 5/26/16.
 * Scorekeeping for the victory point cards in a user's inventory
 */
public class InventoryScoreKeeper implements ScoreKeeper {

    private final Map<Username, InventoryCounter> counts;

    private final Stack<ScoreEvent> history;

    public InventoryScoreKeeper(PlayerPool players) {
        counts = new HashMap<>();
        for (Username username : players)
            if (username != null)
                counts.put(username, new InventoryCounter(username));
        history = new Stack<>();
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        ScoreEvent event = history.pop();
        try {
            InventoryCounter counter = counts.get(event.getOrigin());
            counter.undoGainCard((DevelopmentCard) event.getPayload());
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public EventConsumerProblem test(ScoreEvent event) {
        if (event == null)
            return new EventConsumerProblem("No event");
        if (!counts.containsKey(event.getOrigin()))
            return new EventConsumerProblem("No player");
        switch (event.getType()) {
            case Buy_Development:
                break;
            default:
                return new EventConsumerProblem("Uninteresting");
        }
        return null;
    }

    @Override
    public void execute(ScoreEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(event, problem);
        try {
            history.push(event);
            InventoryCounter counter = counts.get(event.getOrigin());
            counter.gainCard((DevelopmentCard) event.getPayload());
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public ScoreReport score(GameRules rules) {
        List<PlayerScoreReport> reports = counts.values().stream()
                .map(counter -> counter.score(rules))
                .collect(Collectors.toList());
        return new SimpleScoreReport(reports);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        InventoryScoreKeeper other = (InventoryScoreKeeper) o;

        if (!counts.equals(other.counts)) return false;
        return history.equals(other.history);

    }

    @Override
    public int hashCode() {
        int result = counts.hashCode();
        result = (31 * result) + history.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "InventoryScoreKeeper" + counts;
    }
}
