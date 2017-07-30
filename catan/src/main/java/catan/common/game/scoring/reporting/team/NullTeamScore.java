package catan.common.game.scoring.reporting.team;

import catan.common.game.scoring.reporting.scores.NullScoreReport;
import catan.common.game.scoring.reporting.scores.ScoreReport;
import catan.common.game.teams.TeamColor;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by greg on 5/28/16.
 * An empty team score card.
 */
public final class NullTeamScore implements TeamScoreReport {

    public static final NullTeamScore INSTANCE = new NullTeamScore();

    private NullTeamScore() {
    }

    @Override
    public ScoreReport getScoreReport(TeamColor teamColor) {
        return NullScoreReport.INSTANCE;
    }

    @Override
    public Iterator<TeamColor> iterator() {
        return new Iterator<TeamColor>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public TeamColor next() {
                throw new NoSuchElementException();
            }
        };
    }
}
