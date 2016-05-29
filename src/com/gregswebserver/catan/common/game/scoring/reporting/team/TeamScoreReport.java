package com.gregswebserver.catan.common.game.scoring.reporting.team;

import com.gregswebserver.catan.common.game.scoring.reporting.scores.ScoreReport;
import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by greg on 5/27/16.
 * A score report containing information coallated by team.
 */
public interface TeamScoreReport extends Iterable<TeamColor> {

    ScoreReport getScoreReport(TeamColor teamColor);
}
