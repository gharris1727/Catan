package catan.common.game.gamestate;

import catan.common.crypto.Username;
import catan.common.event.EventConsumerException;
import catan.common.game.GameTestUtils;
import catan.common.structure.game.GameSettings;
import catan.junit.FuzzTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;

/**
 * Created by greg on 12/21/16.
 * Test cases for the randomizer state, in order to address inconsistencies.
 */
@Category(FuzzTests.class)
public class RandomizerStateTest {

    private static final int RUNS = 100000;

    private final Username greg = new Username("Greg");
    private final Username bob = new Username("Bob");
    private final GameSettings gameSettings = GameTestUtils.createSettings(
        System.nanoTime(), Arrays.asList(greg, bob));

    @Test
    public void testDiceRolls() throws EventConsumerException {
        RandomizerState a = new RandomizerState(gameSettings);
        RandomizerState b = new RandomizerState(gameSettings);
        for (int i = 0; i < RUNS; i++) {
            GameStateEvent event = new GameStateEvent(null, GameStateEventType.Roll_Dice, a.getDiceRoll());
            Assert.assertNull(a.test(event));
            Assert.assertEquals(a.getDiceRoll(), b.getDiceRoll());
            Assert.assertNull(b.test(event));
            Assert.assertEquals(a.getDiceRoll(), b.getDiceRoll());
            a.execute(event);
            b.execute(event);
            Assert.assertEquals(a.getDiceRoll(), b.getDiceRoll());
            a.undo();
            b.undo();
            Assert.assertEquals(a.getDiceRoll(), b.getDiceRoll());
            a.execute(event);
            b.execute(event);
            Assert.assertEquals(a.getDiceRoll(), b.getDiceRoll());
        }
        RandomizerStateEqualityTester.INSTANCE.assertEquals(a, b);
    }
}
