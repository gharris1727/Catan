package catan.common.game.players;

import catan.common.game.EqualityTester;
import catan.common.game.gamestate.DevelopmentCard;
import catan.common.game.util.EnumCounterEqualityTester;
import catan.common.game.util.GameResource;
import org.junit.Assert;

/**
 * Created by greg on 7/9/16.
 * An EqualityTester for the Player class.
 */
public class PlayerEqualityTester implements EqualityTester<Player> {

    public static final PlayerEqualityTester INSTANCE = new PlayerEqualityTester();

    private final EnumCounterEqualityTester<GameResource> inventoryEqualityTester = new EnumCounterEqualityTester<>();
    private final EnumCounterEqualityTester<DevelopmentCard> cardsEqualityTester = new EnumCounterEqualityTester<>();

    private PlayerEqualityTester() {
    }

    @Override
    public void assertEquals(Player a, Player b) {
        if (a == b)
            return;

        Assert.assertEquals(a.getClass(), b.getClass());
        Assert.assertEquals(a.getName(), b.getName());
        Assert.assertEquals(a.getTeamColor(), b.getTeamColor());
        inventoryEqualityTester.assertEquals(a.getInventory(), b.getInventory());
        cardsEqualityTester.assertEquals(a.getBoughtCards(), b.getBoughtCards());
        cardsEqualityTester.assertEquals(a.getDevelopmentCards(), b.getDevelopmentCards());
        Assert.assertEquals(a.getTrades(), b.getTrades());
    }
}
