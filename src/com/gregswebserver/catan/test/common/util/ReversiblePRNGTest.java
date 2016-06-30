package com.gregswebserver.catan.test.common.util;

import com.gregswebserver.catan.common.util.ReversiblePRNG;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by greg on 3/12/16.
 * Test the randomness of the ReversiblePRNG
 */
public class ReversiblePRNGTest {

    private long seed;
    private int count;
    private int max;

    @Before
    public void setParameters() {
        seed = System.nanoTime();
        count = 1000000;
        max = 113;
    }

    @Test
    public void testValid() {
        ReversiblePRNG prng = new ReversiblePRNG(seed);
        for (int i = 0; i < count; i++) {
            int generated = prng.nextInt(max);
            assertTrue(generated >= 0);
            assertTrue(generated < max);
        }
    }

    @Test
    public void testDeterministic() {
        ReversiblePRNG a = new ReversiblePRNG(seed);
        ReversiblePRNG b = new ReversiblePRNG(seed);
        for (int i = 0; i < count; i++) {
            assertEquals(a.nextInt(max), b.nextInt(max));
        }
    }

    @Test
    public void testReversible() {
        ReversiblePRNG prng = new ReversiblePRNG(seed);
        ArrayList<Integer> generated = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            generated.add(prng.nextInt(max));
            assertEquals((int) generated.get(i), prng.prevInt(max));
            assertEquals((int) generated.get(i), prng.nextInt(max));
        }
    }

    @Test
    public void testDistribution() {
        int expected = count / max;
        int[] distribution = new int[max];
        ReversiblePRNG prng = new ReversiblePRNG();
        for (int i = 0; i < count; i++)
            distribution[prng.nextInt(max)]++;
        float chisquare = 0.0f;
        for (int i = 0; i < max; i++) {
            double variance = (double) (distribution[i] - expected);
            chisquare += variance * variance / expected;
        }
        System.out.println("X^2 with " + (max -1) + " degrees of freedom: " + chisquare);
    }

}