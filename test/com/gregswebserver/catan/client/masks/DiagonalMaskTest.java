package com.gregswebserver.catan.client.masks;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class DiagonalMaskTest {

    private final Dimension testSize = new Dimension(16, 54);

    @Test
    public void testGetWidth() throws Exception {
        DiagonalMask test = new DiagonalMask(testSize);
        assertEquals(36, test.getWidth());
    }

    @Test
    public void testGetHeight() throws Exception {
        DiagonalMask test = new DiagonalMask(testSize);
        assertEquals(54, test.getHeight());
    }

    @Test
    public void testGetCenter() throws Exception {
        DiagonalMask test = new DiagonalMask(testSize);
        assertEquals(46, test.getCenter());
    }

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {23, 23, 22, 22, 21, 21, 20, 20, 19, 19, 18, 18, 17, 17, 16, 16, 15, 15, 14, 14, 13, 13, 12, 12, 11, 11, 10, 10, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 1, 3, 4, 6, 7, 9, 10};
        ArrayList<Integer> test = new DiagonalMask(testSize).getLeftPadding();
        assertEquals(expected.length, test.size());
        for (int i = 0; i < test.size(); i++) {
            assertEquals("" + i, expected[i], (int) test.get(i));
        }
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {3, 4, 7, 8, 11, 12, 15, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 15, 12, 11, 8, 7, 4, 3};
        ArrayList<Integer> test = new DiagonalMask(testSize).getLineWidth();
        assertEquals(expected.length, test.size());
        for (int i = 0; i < test.size(); i++) {
            assertEquals("" + i, expected[i], (int) test.get(i));
        }
    }
}