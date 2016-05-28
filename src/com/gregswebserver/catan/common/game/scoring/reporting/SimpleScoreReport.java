package com.gregswebserver.catan.common.game.scoring.reporting;

import com.gregswebserver.catan.common.crypto.Username;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 5/27/16.
 * A score report that has just the most basic form of a score report.
 */
public class SimpleScoreReport implements ScoreReport {

    private final Map<Username, PlayerScoreReport> scores;

    public SimpleScoreReport(Collection<PlayerScoreReport> reports) {
        scores = new HashMap<>();
        for (PlayerScoreReport report : reports)
            scores.put(report.getUsername(), report);
    }

    @Override
    public Collection<PlayerScoreReport> getPlayerReports() {
        return scores.values();
    }

    @Override
    public PlayerScoreReport getPlayerReport(Username username) {
        return scores.get(username);
    }
}
