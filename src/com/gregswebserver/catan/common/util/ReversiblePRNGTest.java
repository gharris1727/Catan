package com.gregswebserver.catan.common.util;

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
        count = 1000;
        max = Integer.MAX_VALUE;
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
            for (int j = i; j >= 0; j--) {
                assertEquals((int) generated.get(j), prng.prevInt(max));
            }
            for (int j = 0; j <= i; j++) {
                assertEquals((int) generated.get(j), prng.nextInt(max));
            }
        }
    }

    @Test
    public void testDistribution() {
        int runs = 1000000;
        int buckets = 101;
        int expected = runs / buckets;
        int[] distribution = new int[buckets];
        ReversiblePRNG prng = new ReversiblePRNG();
        for (int i = 0; i < runs; i++)
            distribution[prng.nextInt(buckets)]++;
        float chisquare = 0.0f;
        for (int i = 0; i < buckets; i++) {
            double variance = (double) (distribution[i] - expected);
            chisquare += variance * variance / expected;
        }
        System.out.println("X^2 with " + (buckets-1) + " degrees of freedom: " + chisquare);
    }

}