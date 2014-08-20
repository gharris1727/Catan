package com.gregswebserver.catan.client.masks;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DiagonalDownMaskTest {

    @Test
    public void testGetWidth() throws Exception {
        DiagonalDownMask test = new DiagonalDownMask(9, 26);
        assertEquals(19, test.getWidth());
    }

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {5, 3, 1, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11};
        ArrayList<Integer> test = new DiagonalDownMask(9, 26).getLeftPadding();
        for (int i = 0; i < expected.length; i++) {
            assertEquals("" + i, expected[i], (int) test.get(i));
        }
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {3, 5, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 5, 3};
        ArrayList<Integer> test = new DiagonalDownMask(9, 26).getLineWidth();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int) test.get(i));
        }
    }
}