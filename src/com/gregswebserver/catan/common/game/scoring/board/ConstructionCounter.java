package com.gregswebserver.catan.common.game.scoring.board;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScorable;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.player.SimplePlayerScore;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 5/27/16.
 * The construction counts of a specific player.
 */
public class ConstructionCounter implements PlayerScorable {

    private final Username username;
    private int settlements;
    private int cities;
    private int roads;

    ConstructionCounter(Username username) {
        this.username = username;
        settlements = 0;
        cities = 0;
        roads = 0;
    }

    public void buildSettlement() {
        settlements++;
    }

    public void undoBuildSettlement() {
        settlements--;
    }

    public void buildCity() {
        cities++;
        settlements--;
    }

    public void undoBuildCity() {
        cities--;
        settlements++;
    }

    public void buildRoad() {
        roads++;
    }

    public void undoBuildRoad() {
        roads--;
    }

    @Override
    public PlayerScoreReport score(GameRules rules) {
        Map<String, Integer> points = new HashMap<>();
        points.put("game.scoring.construction.settlement", settlements * rules.getSettlementPoints());
        points.put("game.scoring.construction.city", cities * rules.getCityPoints());
        points.put("game.scoring.construction.road", roads * rules.getPathPoints());
        return new SimplePlayerScore(username, points);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstructionCounter that = (ConstructionCounter) o;

        if (settlements != that.settlements) return false;
        if (cities != that.cities) return false;
        if (roads != that.roads) return false;
        return username.equals(that.username);

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + settlements;
        result = 31 * result + cities;
        result = 31 * result + roads;
        return result;
    }

    @Override
    public String toString() {
        return "ConstructionCounter{" +
            "username=" + username +
            ", settlements=" + settlements +
            ", cities=" + cities +
            ", roads=" + roads +
            '}';
    }
}
