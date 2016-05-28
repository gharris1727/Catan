package com.gregswebserver.catan.common.game.scoring.board;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.scoring.reporting.PlayerScorable;
import com.gregswebserver.catan.common.game.scoring.reporting.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.SimplePlayerScore;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;

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
        int points = 0;
        points += settlements * rules.getSettlementPoints();
        points += cities * rules.getCityPoints();
        points += roads * rules.getPathPoints();
        return new SimplePlayerScore(username, points);
    }
}
