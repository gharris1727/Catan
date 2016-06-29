package com.gregswebserver.catan.common.game.gamestate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 6/28/16.
 * Test class for the DiceState randomizer
 */
public class DiceStateTest {

    long seed = 1232135L;
    int runs = 1000000;

    @Test
    public void test() {
        DiceState a = new DiceState(seed);
        DiceState b = new DiceState(seed);
        for (int i = 0; i < runs; i++) {
            DiceRoll roll = a.get();
            assertEquals(roll, b.get());
            assertEquals(roll, a.next());
            assertEquals(roll, b.next());
            assertEquals(roll, a.prev());
            assertEquals(roll, b.prev());
            a.next();
            b.next();
        }
    }
}
