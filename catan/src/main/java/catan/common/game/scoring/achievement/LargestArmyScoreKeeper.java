package catan.common.game.scoring.achievement;

import catan.common.event.EventConsumerException;
import catan.common.event.EventConsumerProblem;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.players.PlayerPool;
import catan.common.game.scoring.ScoreEvent;
import catan.common.game.scoring.ScoreKeeper;
import catan.common.game.scoring.reporting.scores.NullScoreReport;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.scoring.rules.GameRules;

import java.util.Stack;

/**
 * Created by greg on 5/26/16.
 * A leaderboard tracking the largest army in a catan game.
 */
public class LargestArmyScoreKeeper implements ScoreKeeper {

    private final Stack<ScoreEvent> history;

    public LargestArmyScoreKeeper(PlayerPool players) {
        history = new Stack<>();
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        ScoreEvent event = history.pop();
        try {
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public EventConsumerProblem test(ScoreEvent event) {
        if (event == null)
            return new EventConsumerProblem("No event");
        switch (event.getType()) {
            case Play_Development:
                if (event.getPayload() != DevelopmentCard.Knight)
                    return new EventConsumerProblem("Uninterested");
                break;
            default:
                return new EventConsumerProblem("Uninterested");
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
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public ScoreReport score(GameRules rules) {
        //TODO: implement counting scores of largest army.
        return NullScoreReport.INSTANCE;
    }
}
