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

    private static final int RUNS = 100000;
    private static final long SEED = System.nanoTime();

    @Test
    public void testStateCreation() {
        new DiceState(SEED);
    }

    @Test
    public void testUndo() {
        DiceState a = new DiceState(SEED);
        for (int i = 0; i < RUNS; i++) {
            DiceRoll roll = a.get();
            assertEquals(roll, a.next());
            assertEquals(roll, a.prev());
            assertEquals(roll, a.get());
            a.next();
        }
    }

    @Test
    public void testDeterminism() {
        DiceState a = new DiceState(SEED);
        DiceState b = new DiceState(SEED);
        for (int i = 0; i < RUNS; i++)
            assertEquals(a.next(), b.next());
    }
}
