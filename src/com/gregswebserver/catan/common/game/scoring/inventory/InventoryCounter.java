package com.gregswebserver.catan.common.game.scoring.inventory;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.game.gamestate.DevelopmentCard;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScorable;
import com.gregswebserver.catan.common.game.scoring.reporting.player.PlayerScoreReport;
import com.gregswebserver.catan.common.game.scoring.reporting.player.SimplePlayerScore;
import com.gregswebserver.catan.common.game.scoring.rules.GameRules;
import com.gregswebserver.catan.common.game.util.EnumCounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 5/27/16.
 * Class for counting the number of scoring development cards in a user's inventory.
 */
public class InventoryCounter implements PlayerScorable {

    private final Username username;
    private final EnumCounter<DevelopmentCard> counts;

    public InventoryCounter(Username username) {
        this.username = username;
        counts = new EnumCounter<>(DevelopmentCard.class);
    }

    public void gainCard(DevelopmentCard card) {
        counts.increment(card, 1);
    }

    public void undoGainCard(DevelopmentCard card) {
        counts.decrement(card, 1);
    }

    @Override
    public PlayerScoreReport score(GameRules rules) {
        Map<String, Integer> points = new HashMap<>();
        for (DevelopmentCard card : DevelopmentCard.values())
            points.put("inventory."+ card.toString(), counts.get(card) * rules.getDevelopmentCardPoints(card));
        return new SimplePlayerScore(username, points);
    }
}