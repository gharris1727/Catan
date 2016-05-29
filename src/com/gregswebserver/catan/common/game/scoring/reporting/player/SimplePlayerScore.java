package com.gregswebserver.catan.common.game.scoring.reporting.player;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.ScoreException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by greg on 5/27/16.
 * A simple player score report where all information is preprocessed.
 */
public class SimplePlayerScore implements PlayerScoreReport {

    private final Username username;
    private final Map<String, Integer> breakdown;
    private final int points;

    public SimplePlayerScore(Username username, Map<String, Integer> breakdown) {
        this.username = username;
        this.breakdown = breakdown;
        int points = 0;
        for (Integer i : breakdown.values())
            points += i;
        this.points = points;
    }

    public SimplePlayerScore(PlayerScoreReport a, PlayerScoreReport b) throws ScoreException {
        if (!b.getUsername().equals(a.getUsername()))
            throw new ScoreException("Mismatched usernames");
        username = a.getUsername();
        breakdown = new HashMap<>();
        int points = 0;
        for (String category : a) {
            points += a.getPoints(category);
            breakdown.put(category, a.getPoints(category));
        }
        for (String category : b) {
            int categoryPoints = b.getPoints(category);
            points += categoryPoints;
            if (breakdown.containsKey(category))
                categoryPoints += breakdown.get(category);
            breakdown.put(category, categoryPoints);
        }
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

    @Override
    public int getPoints(String category) {
        return breakdown.get(category);
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            private final Iterator<String> it = breakdown.keySet().iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public String next() {
                return it.next();
            }
        };
    }
}
