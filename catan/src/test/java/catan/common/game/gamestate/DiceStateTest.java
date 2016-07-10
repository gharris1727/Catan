package catan.common.game.gamestate;

import catan.junit.FuzzTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 6/28/16.
 * Test class for the DiceState randomizer
 */
@Category(FuzzTests.class)
public class DiceStateTest {

    private static final int runs = 100000;
    private static final long seed = System.nanoTime();

    @Test
    public void testStateCreation() {
        new DiceState(seed);
    }

    @Test
    public void testUndo() {
        DiceState a = new DiceState(seed);
        for (int i = 0; i < runs; i++) {
            DiceRoll roll = a.get();
            assertEquals(roll, a.next());
            assertEquals(roll, a.prev());
            a.next();
        }
    }

    @Test
    public void testDeterminism() {
        DiceState a = new DiceState(seed);
        DiceState b = new DiceState(seed);
        for (int i = 0; i < runs; i++)
            assertEquals(a.next(), b.next());
    }
}
