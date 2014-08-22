package com.gregswebserver.catan.client.masks;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class RoundedMaskTest {

    private final Dimension testSize = new Dimension(6, 5);

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {4, 2, 1, 1, 0, 0, 0, 1, 1, 2, 4};
        ArrayList<Integer> test = new RoundedMask(testSize).getLeftPadding();
        assertEquals(expected.length, test.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("" + i, expected[i], (int) test.get(i));
        }
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {5, 9, 11, 11, 13, 13, 13, 11, 11, 9, 5};
        ArrayList<Integer> test = new RoundedMask(testSize).getLineWidth();
        assertEquals(expected.length, test.size());
        for (int i = 0; i < expected.length; i++) {
            assertEquals("" + i, expected[i], (int) test.get(i));
        }
    }
}