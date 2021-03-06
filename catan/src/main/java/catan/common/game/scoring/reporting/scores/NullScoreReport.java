package catan.common.game.scoring.reporting.scores;

import catan.common.crypto.Username;
import catan.common.game.scoring.reporting.player.NullPlayerScore;
import catan.common.game.scoring.reporting.player.PlayerScoreReport;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by greg on 5/28/16.
 * A score report with no contents.
 */
public final class NullScoreReport implements ScoreReport {

    @Override
    public PlayerScoreReport getPlayerReport(Username username) {
        return new NullPlayerScore();
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public Iterator<Username> iterator() {
        return new Iterator<Username>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Username next() {
                throw new NoSuchElementException();
            }
        };
    }
}
