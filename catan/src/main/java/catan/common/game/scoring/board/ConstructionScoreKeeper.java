package catan.common.game.scoring.board;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.players.PlayerPool;
import catan.common.game.scoring.ScoreEvent;
import catan.common.game.scoring.ScoreKeeper;
import catan.common.game.scoring.reporting.player.PlayerScoreReport;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.scoring.reporting.scores.SimpleScoreReport;
import catan.common.game.scoring.rules.GameRules;

import java.util.*;

/**
 * Created by greg on 5/26/16.
 * A scorekeeper to track points gained from structures built.
 */
public class ConstructionScoreKeeper implements ScoreKeeper {

    private final Map<Username, ConstructionCounter> counts;

    private final Stack<ScoreEvent> history;

    public ConstructionScoreKeeper(PlayerPool players) {
        counts = new HashMap<>();
        for (Username username : players)
            if (username != null)
                counts.put(username, new ConstructionCounter(username));
        history = new Stack<>();
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        ScoreEvent event = history.pop();
        try {
            ConstructionCounter counter = counts.get(event.getOrigin());
            switch (event.getType()) {
                case Build_Settlement:
                    counter.undoBuildSettlement();
                    break;
                case Build_City:
                    counter.undoBuildCity();
                    break;
                case Build_Road:
                    counter.undoBuildRoad();
                    break;
                default:
                    throw new EventConsumerException("Inconsistent ScoreEvent Undo");
            }
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
            case Build_Settlement:
            case Build_City:
            case Build_Road:
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
            ConstructionCounter counter = counts.get(event.getOrigin());
            switch (event.getType()) {
                case Build_Settlement:
                    counter.buildSettlement();
                    break;
                case Build_City:
                    counter.buildCity();
                    break;
                case Build_Road:
                    counter.buildRoad();
                    break;
                default:
                    throw new EventConsumerException("Inconsistent ScoreEvent Exec");
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public ScoreReport score(GameRules rules) {
        Collection<PlayerScoreReport> scores = new ArrayList<>();
        for (ConstructionCounter counter : counts.values())
            scores.add(counter.score(rules));
        return new SimpleScoreReport(scores);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstructionScoreKeeper that = (ConstructionScoreKeeper) o;

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
        return "ConstructionScoreKeeper" + counts;
    }
}
