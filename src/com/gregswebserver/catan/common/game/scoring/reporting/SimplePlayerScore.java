package com.gregswebserver.catan.common.game.scoring.reporting;

import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by greg on 5/27/16.
 * A simple player score report where all information is preprocessed.
 */
public class SimplePlayerScore implements PlayerScoreReport {

    private final Username username;
    private final int points;

    public SimplePlayerScore(Username username, int points) {
        this.username = username;
        this.points = points;
    }

    @Override
    public Username getUsername() {
        return username;
    }

    @Override
    public int getPoints() {
        return points;
    }
}
