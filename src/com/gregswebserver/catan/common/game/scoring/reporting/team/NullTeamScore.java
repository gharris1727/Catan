package com.gregswebserver.catan.common.game.scoring.reporting.team;

import com.gregswebserver.catan.common.game.scoring.reporting.scores.NullScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.Iterator;

/**
 * Created by greg on 5/28/16.
 * An empty team score card.
 */
public class NullTeamScore implements TeamScoreReport {

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
                return null;
            }
        };
    }
}
