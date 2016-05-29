package com.gregswebserver.catan.common.game.scoring.reporting.player;

import com.gregswebserver.catan.common.crypto.Username;

import java.util.Iterator;

/**
 * Created by greg on 5/28/16.
 * A dummy player score object.
 */
public class NullPlayerScore implements PlayerScoreReport {

    public static final NullPlayerScore INSTANCE = new NullPlayerScore();

    private final Username username;

    private NullPlayerScore() {
        username = new Username("Nobody");
    }

    @Override
    public Username getUsername() {
        return username;
    }

    @Override
    public int getPoints(String category) {
        return 0;
    }

    @Override
    public int getPoints() {
        return 0;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public String next() {
                return null;
            }
        };
    }
}
