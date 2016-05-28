package com.gregswebserver.catan.common.game.scoring.reporting;

import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by greg on 5/27/16.
 * A ScoreReport associated with a single player.
 */
public interface PlayerScoreReport {

    Username getUsername();

    int getPoints();
}
