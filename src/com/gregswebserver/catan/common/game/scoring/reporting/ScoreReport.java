package com.gregswebserver.catan.common.game.scoring.reporting;

import com.gregswebserver.catan.common.crypto.Username;

import java.util.Collection;

/**
 * Created by greg on 5/27/16.
 * A report about what contributes to a player's score.
 */
public interface ScoreReport {

    Collection<PlayerScoreReport> getPlayerReports();

    PlayerScoreReport getPlayerReport(Username username);

}
