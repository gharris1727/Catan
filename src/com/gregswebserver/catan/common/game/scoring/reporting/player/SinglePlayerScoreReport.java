package com.gregswebserver.catan.common.game.scoring.reporting.player;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by greg on 5/28/16.
 * A ScoreReport that consists of just a simgle player.
 */
public class SinglePlayerScoreReport implements ScoreReport {

    private final PlayerScoreReport report;

    public SinglePlayerScoreReport(PlayerScoreReport report) {
        this.report = report;
    }

    @Override
    public PlayerScoreReport getPlayerReport(Username username) {
        return report;
    }

    @Override
    public int getPoints() {
        return report.getPoints();
    }

    @Override
    public Iterator<Username> iterator() {
        return new Iterator<Username>() {

            private boolean hasNext = true;
            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Username next() {
                if (!hasNext)
                    throw new NoSuchElementException();
                hasNext = false;
                return report.getUsername();
            }
        };
    }

}
