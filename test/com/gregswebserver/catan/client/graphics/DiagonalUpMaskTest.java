package com.gregswebserver.catan.client.graphics;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DiagonalUpMaskTest {

    @Test
    public void testGetWidth() throws Exception {
        DiagonalUpMask test = new DiagonalUpMask(26, 9);
        assertEquals(19, test.getWidth());
    }

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {11, 11, 10, 10, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 1, 3, 5};
        ArrayList<Integer> test = new DiagonalUpMask(26, 9).getLeftPadding();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int) test.get(i));
        }
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {3, 5, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 5, 3};
        ArrayList<Integer> test = new DiagonalUpMask(26, 9).getLineWidth();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int) test.get(i));
        }
    }
}