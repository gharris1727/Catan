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
public final class PlayerEqualityTester implements EqualityTester<Player> {

    public static final PlayerEqualityTester INSTANCE = new PlayerEqualityTester();

    private final EnumCounterEqualityTester<GameResource> inventoryEqualityTester = new EnumCounterEqualityTester<>();
    private final EnumCounterEqualityTester<DevelopmentCard> cardsEqualityTester = new EnumCounterEqualityTester<>();

    private PlayerEqualityTester() {
    }

    @Override
    public void assertEquals(Player expected, Player actual) {
        if (expected == actual)
            return;

        Assert.assertEquals(expected.getClass(), actual.getClass());
        Assert.assertEquals(expected.getName(), actual.getName());
        Assert.assertEquals(expected.getTeamColor(), actual.getTeamColor());
        inventoryEqualityTester.assertEquals(expected.getInventory(), actual.getInventory());
        cardsEqualityTester.assertEquals(expected.getBoughtCards(), actual.getBoughtCards());
        cardsEqualityTester.assertEquals(expected.getDevelopmentCards(), actual.getDevelopmentCards());
        Assert.assertEquals(expected.getTrades(), actual.getTrades());
    }
}
