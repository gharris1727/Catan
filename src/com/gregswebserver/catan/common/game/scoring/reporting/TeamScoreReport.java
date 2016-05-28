package com.gregswebserver.catan.common.game.scoring.reporting;

import com.gregswebserver.catan.common.game.teams.TeamColor;

/**
 * Created by greg on 5/27/16.
 * A score report containing information coallated by team.
 */
public interface TeamScoreReport {

    TeamColor getTeam();

    int getPoints();
}
