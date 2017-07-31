package catan.common.game.scoring;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.event.ReversibleEventConsumer;
import catan.common.game.board.GameBoard;
import catan.common.game.players.PlayerPool;
import catan.common.game.scoring.board.ConstructionScoreKeeper;
import catan.common.game.scoring.inventory.InventoryScoreKeeper;
import catan.common.game.scoring.reporting.ScoreException;
import catan.common.game.scoring.reporting.scores.NullScoreReport;
import catan.common.game.scoring.reporting.scores.Scorable;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.scoring.reporting.scores.SimpleScoreReport;
import catan.common.game.scoring.reporting.team.NullTeamScore;
import catan.common.game.scoring.reporting.team.SimpleTeamReport;
import catan.common.game.scoring.reporting.team.TeamScorable;
import catan.common.game.scoring.reporting.team.TeamScoreReport;
import catan.common.game.scoring.rules.GameRules;
import catan.common.game.teams.TeamPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by greg on 5/27/16.
 * A collection of ScoreKeeper objects that
 */
public class ScoreState implements ReversibleEventConsumer<ScoreEvent>, TeamScorable {

    final PlayerPool players;
    final List<ScoreKeeper> listeners;
    final Stack<List<ScoreKeeper>> history;

    public ScoreState(GameBoard board, PlayerPool players, TeamPool teams) {
        this.players = players;
        listeners = Arrays.asList(
            //TODO: after fixing the achievements re-enable them.
            //new LargestArmyScoreKeeper(players),
            //new LongestRoadScoreKeeper(board),
            new ConstructionScoreKeeper(players),
            new InventoryScoreKeeper(players)
        );
        history = new Stack<>();
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        List<ScoreKeeper> affected = history.pop();
        try {
            for (ScoreKeeper listener : affected)
                listener.undo();
        } catch (Exception e) {
            throw new EventConsumerException("Unable to undo ScoreEvent", e);
        }
    }

    @Override
    public EventConsumerProblem test(ScoreEvent event) {
        //non-null events are always successfully applied to this listener, but not necessarily its child listeners.
        return (event == null) ? new EventConsumerProblem("No event") : null;
    }

    @Override
    public void execute(ScoreEvent event) throws EventConsumerException {
        EventConsumerProblem problem = test(event);
        if (problem != null)
            throw new EventConsumerException(event, problem);
        try {
            List<ScoreKeeper> affected = new ArrayList<>();
            history.push(affected);
            for (ScoreKeeper listener : listeners) {
                if (listener.test(event) == null) {
                    //If it was successful, we should apply, and track that it was applied.
                    listener.execute(event);
                    affected.add(listener);
                }
            }
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public TeamScoreReport score(GameRules rules) throws ScoreException {
        if (listeners.isEmpty())
            return new NullTeamScore();
        ScoreReport report = new NullScoreReport();
        for (Scorable scorable : listeners) {
                report = new SimpleScoreReport(report, scorable.score(rules));
        }
        return new SimpleTeamReport(players, report);
    }

}
