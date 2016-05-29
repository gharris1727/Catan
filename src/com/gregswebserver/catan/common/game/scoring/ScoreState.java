package com.gregswebserver.catan.common.game.scoring;

import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.event.ReversibleEventConsumer;
import com.gregswebserver.catan.common.game.board.GameBoard;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.scoring.board.ConstructionScoreKeeper;
import com.gregswebserver.catan.common.game.scoring.inventory.InventoryScoreKeeper;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreException;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.NullScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.Scorable;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.SimpleScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.team.NullTeamScore;
import com.gregswebserver.catan.common.game.scoring.reporting.team.SimpleTeamReport;
import com.gregswebserver.catan.common.game.scoring.reporting.team.TeamScorable;
import com.gregswebserver.catan.common.game.scoring.reporting.team.TeamScoreReport;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.teams.TeamPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by greg on 5/27/16.
 * A collection of ScoreKeeper objects that
 */
public class ScoreState implements ReversibleEventConsumer<ScoreEvent>, TeamScorable {

    private final PlayerPool players;
    private final List<ScoreKeeper> listeners;

    private final Stack<List<ScoreKeeper>> history;

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
    public void test(ScoreEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        //Events are always successfully applied to this listener, but not necessarily other listeners.
    }

    @Override
    public void execute(ScoreEvent event) throws EventConsumerException {
        test(event);
        try {
            ArrayList<ScoreKeeper> affected = new ArrayList<>();
            history.push(affected);
            for (ScoreKeeper listener : listeners) {
                boolean successful = true;
                try {
                    //Check to see if this event can be applied to this listener.
                    listener.test(event);
                } catch (Exception e) {
                    //If there is an exception, it was not successful.
                    successful = false;
                }
                if (successful) {
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
            return NullTeamScore.INSTANCE;
        ScoreReport report = NullScoreReport.INSTANCE;
        for (Scorable scorable : listeners) {
                report = new SimpleScoreReport(report, scorable.score(rules));
        }
        return new SimpleTeamReport(players, report);
    }
}
