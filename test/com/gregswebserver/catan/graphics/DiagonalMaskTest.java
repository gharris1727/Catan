package com.gregswebserver.catan.graphics;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class DiagonalMaskTest {

    @Test
    public void testGetWidth() throws Exception {
        DiagonalMask test = new DiagonalMask(26, 9);
        assertEquals(19, test.getWidth());
    }

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {11, 11, 10, 10, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 1, 3, 5};
        Iterator<Integer> test = new DiagonalMask(26, 9).getLeftPadding();
        for (int i = 0; i < expected.length; i++) {
            assertTrue("i: " + i, test.hasNext());
            assertEquals("i: " + i, expected[i], (int) test.next());
        }
        assertFalse(test.hasNext());
        assertNull(test.next());
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {3, 5, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 5, 3};
        Iterator<Integer> test = new DiagonalMask(26, 9).getLineWidth();
        for (int i = 0; i < expected.length; i++) {
            assertTrue("i: " + i, test.hasNext());
            assertEquals("i: " + i, expected[i], (int) test.next());
        }
        assertFalse(test.hasNext());
        assertNull(test.next());
    }
}