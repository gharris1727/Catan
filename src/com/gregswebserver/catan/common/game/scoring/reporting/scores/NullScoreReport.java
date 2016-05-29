package com.gregswebserver.catan.common.game.scoring.reporting.scores;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.player.NullPlayerScore;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;

import java.util.Iterator;

/**
 * Created by greg on 5/28/16.
 * A score report with no contents.
 */
public class NullScoreReport implements ScoreReport {

    public static final NullScoreReport INSTANCE = new NullScoreReport();

    private NullScoreReport() {
    }

    @Override
    public PlayerScoreReport getPlayerReport(Username username) {
        return NullPlayerScore.INSTANCE;
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
                return null;
            }
        };
    }
}
