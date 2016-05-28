package com.gregswebserver.catan.common.game.scoring.achievement;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreKeeper;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;

import java.util.Comparator;
import java.util.Stack;

/**
 * Created by greg on 5/26/16.
 * A leaderboard tracking the largest army in a catan game.
 */
public class LargestArmyScoreKeeper implements ScoreKeeper {

    private final Scoreboard<Username, Integer, Integer> scores;
    private final Stack<ScoreEvent> history;

    public LargestArmyScoreKeeper(PlayerPool players) {
        //noinspection Convert2Diamond
        scores = new Scoreboard<Username, Integer, Integer>(Comparator.reverseOrder());
        history = new Stack<>();
        for (Username username : players)
            scores.add(username, 0, 0);
    }

    @Override
    public void undo() throws EventConsumerException {
        if (history.isEmpty())
            throw new EventConsumerException("No event");
        ScoreEvent event = history.pop();
        try {
            Username username = event.getOrigin();
            int score = scores.removeKey(username);
            scores.add(username, score - 1, history.size());
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public void test(ScoreEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        switch (event.getType()) {
            case Play_Development:
                if (event.getPayload() != DevelopmentCard.Knight)
                    throw new EventConsumerException("Uninterested");
                break;
            default:
                throw new EventConsumerException("Uninterested");
        }
    }

    @Override
    public void execute(ScoreEvent event) throws EventConsumerException {
        test(event);
        try {
            history.push(event);
            Username username = event.getOrigin();
            int score = scores.removeKey(username);
            scores.add(username, score + 1, history.size());
        } catch (Exception e) {
            throw new EventConsumerException(event, e);
        }
    }

    @Override
    public ScoreReport score(GameRules rules) {
        //TODO: implement counting scores of largest army.
        return null;
    }
}
