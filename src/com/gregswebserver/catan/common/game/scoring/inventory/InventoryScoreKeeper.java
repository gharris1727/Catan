package com.gregswebserver.catan.common.game.scoring.inventory;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventConsumerException;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.scoring.ScoreEvent;
import com.gregswebserver.catan.common.game.scoring.ScoreKeeper;
import com.gregswebserver.catan.common.game.scoring.reporting.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.SimpleScoreReport;
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
    public void test(ScoreEvent event) throws EventConsumerException {
        if (event == null)
            throw new EventConsumerException("No event");
        if (!counts.containsKey(event.getOrigin()))
            throw new EventConsumerException("No player");
        switch (event.getType()) {
            case Buy_Development:
                break;
            default:
                throw new EventConsumerException("Uninteresting");
        }
    }

    @Override
    public void execute(ScoreEvent event) throws EventConsumerException {
        test(event);
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
}
