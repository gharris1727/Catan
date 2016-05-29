package com.gregswebserver.catan.common.game.scoring.reporting.team;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.players.PlayerPool;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreException;
import com.gregswebserver.catan.common.game.scoring.reporting.player.SinglePlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.scores.SimpleScoreReport;
import com.gregswebserver.catan.common.game.teams.TeamColor;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 5/28/16.
 * A team report that compiles it's data at construction from a given aggregate score report.
 */
public class SimpleTeamReport implements TeamScoreReport {

    private final Map<TeamColor, ScoreReport> reports;

    public SimpleTeamReport(PlayerPool players, ScoreReport aggregate) throws ScoreException {
        reports = new EnumMap<>(TeamColor.class);
        for (Username username : aggregate) {
            TeamColor teamColor = players.getPlayer(username).getTeamColor();
            ScoreReport report = new SinglePlayerScoreReport(aggregate.getPlayerReport(username));
            if (reports.containsKey(teamColor))
                report = new SimpleScoreReport(reports.get(teamColor), report);
            reports.put(teamColor, report);
        }
    }

    @Override
    public ScoreReport getScoreReport(TeamColor teamColor) {
        return reports.get(teamColor);
    }

    @Override
    public Iterator<TeamColor> iterator() {
        return new Iterator<TeamColor>() {

            private final Iterator<TeamColor> it = reports.keySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public TeamColor next() {
                return it.next();
            }
        };
    }
}
