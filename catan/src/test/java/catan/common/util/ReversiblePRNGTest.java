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

    private static final int count = 1000000;
    private static final int max = 113;
    private static final long seed = System.nanoTime();

    @Test
    public void testValid() {
        ReversiblePRNG prng = new ReversiblePRNG(seed);
        for (int i = 0; i < count; i++) {
            int generated = prng.nextInt(max);
            Assert.assertTrue(generated >= 0);
            Assert.assertTrue(generated < max);
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
        ReversiblePRNG prng = new ReversiblePRNG(seed);
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