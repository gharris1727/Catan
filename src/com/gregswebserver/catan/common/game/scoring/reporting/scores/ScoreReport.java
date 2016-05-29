package com.gregswebserver.catan.common.game.scoring.reporting.scores;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;

/**
 * Created by greg on 5/27/16.
 * A report about what contributes to a player's score.
 */
public interface ScoreReport extends Iterable<Username> {

    PlayerScoreReport getPlayerReport(Username username);

    int getPoints();
}
