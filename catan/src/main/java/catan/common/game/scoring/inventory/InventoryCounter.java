package catan.common.game.scoring.inventory;

import catan.common.crypto.Username;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.scoring.reporting.player.PlayerScorable;
import catan.common.game.scoring.reporting.player.PlayerScoreReport;
import catan.common.game.scoring.reporting.player.SimplePlayerScore;
import catan.common.game.scoring.rules.GameRules;
import catan.common.game.util.EnumAccumulator;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by greg on 5/27/16.
 * Class for counting the number of scoring development cards in a user's inventory.
 */
public class InventoryCounter implements PlayerScorable {

    private final Username username;
    private final EnumAccumulator<DevelopmentCard> counts;

    public InventoryCounter(Username username) {
        this.username = username;
        counts = new EnumAccumulator<>(DevelopmentCard.class);
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
            points.put("game.scoring.inventory."+ card, counts.get(card) * rules.getDevelopmentCardPoints(card));
        return new SimplePlayerScore(username, points);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        InventoryCounter other = (InventoryCounter) o;

        if (!username.equals(other.username)) return false;
        return counts.equals(other.counts);

    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = (31 * result) + counts.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "InventoryCounter{" +
            "username=" + username +
            ", counts=" + counts +
            '}';
    }
}
