package com.gregswebserver.catan.common.game.scoring.inventory;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.EventConsumerProblem;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreKeeper;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.SimpleScoreReport;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;

import java.util.*;

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
            throw new EventConsumerException(problem);
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
        Collection<PlayerScoreReport> scores = new ArrayList<>();
        for (InventoryCounter counter : counts.values())
            scores.add(counter.score(rules));
        return new SimpleScoreReport(scores);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryScoreKeeper that = (InventoryScoreKeeper) o;

        if (!counts.equals(that.counts)) return false;
        return history.equals(that.history);

    }

    @Override
    public int hashCode() {
        int result = counts.hashCode();
        result = 31 * result + history.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "InventoryScoreKeeper" + counts;
    }
}
