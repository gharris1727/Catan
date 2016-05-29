package com.gregswebserver.catan.common.game.scoring.reporting.scores;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreException;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.player.SimplePlayerScore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 5/27/16.
 * A score report that has just the most basic form of a score report.
 */
public class SimpleScoreReport implements ScoreReport {

    private final int points;
    private final Map<Username, PlayerScoreReport> scores;

    public SimpleScoreReport(Collection<PlayerScoreReport> reports) {
        scores = new HashMap<>();
        int points = 0;
        for (PlayerScoreReport report : reports) {
            points += report.getPoints();
            scores.put(report.getUsername(), report);
        }
        this.points = points;
    }

    public SimpleScoreReport(ScoreReport a, ScoreReport b) throws ScoreException {
        scores = new HashMap<>();
        int points = 0;
        for (Username username : a) {
            PlayerScoreReport report = a.getPlayerReport(username);
            points += report.getPoints();
            scores.put(username, report);
        }
        for (Username username : b) {
            PlayerScoreReport report = b.getPlayerReport(username);
            points += report.getPoints();
            if (scores.containsKey(username)) {
                report = new SimplePlayerScore(report, scores.get(username));
            }
            scores.put(username, report);
        }
        this.points = points;
    }

    @Override
    public PlayerScoreReport getPlayerReport(Username username) {
        return scores.get(username);
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public Iterator<Username> iterator() {
        return new Iterator<Username>() {

            private final Iterator<Username> it = scores.keySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Username next() {
                return it.next();
            }
        };
    }
}
