package catan.common.util;

import catan.junit.FuzzTests;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by greg on 3/12/16.
 * Test the randomness of the ReversiblePRNG
 */
@Category(FuzzTests.class)
public class ReversiblePRNGTest {

    private static final int COUNT = 1000000;
    private static final int MAX = 256;
    private static final long SEED = System.nanoTime();

    @Test
    public void testValid() {
        long seed = System.nanoTime();
        ReversiblePRNG prng = new ReversiblePRNG(seed);
        for (int i = 0; i < COUNT; i++) {
            int generated = prng.nextInt(MAX);
            Assert.assertTrue(generated >= 0);
            Assert.assertTrue(generated < MAX);
        }
    }

    @Test
    public void testDeterministic() {
        ReversiblePRNG a = new ReversiblePRNG(SEED);
        ReversiblePRNG b = new ReversiblePRNG(SEED);
        for (int i = 0; i < COUNT; i++) {
            assertEquals(a.nextInt(MAX), b.nextInt(MAX));
        }
    }

    @Test
    public void testReversible() {
        ReversiblePRNG prng = new ReversiblePRNG(SEED);
        ArrayList<Integer> generated = new ArrayList<>(COUNT);
        for (int i = 0; i < COUNT; i++) {
            generated.add(prng.nextInt(MAX));
            assertEquals((int) generated.get(i), prng.prevInt(MAX));
            assertEquals((int) generated.get(i), prng.nextInt(MAX));
        }
    }

    @Test
    public void testDistribution() {
        int expected = COUNT / MAX;
        int[] distribution = new int[MAX];
        ReversiblePRNG prng = new ReversiblePRNG(SEED);
        for (int i = 0; i < COUNT; i++)
            distribution[prng.nextInt(MAX)]++;
        float chisquare = 0.0f;
        for (int i = 0; i < MAX; i++) {
            double variance = (distribution[i] - expected);
            chisquare += (variance * variance) / expected;
        }
        System.out.println("X^2 with " + (MAX - 1) + " degrees of freedom: " + chisquare);
    }

}