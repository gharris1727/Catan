package com.gregswebserver.catan.graphics;

import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class HexagonalMaskTest {

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {12, 11, 11, 10, 10, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12};
        Iterator<Integer> test = new HexagonalMask(56, 48).getLeftPadding();
        for (int i = 0; i < expected.length; i++) {
            assertTrue("i: " + i, test.hasNext());
            assertEquals("i: " + i, expected[i], (int) test.next());
        }
        assertFalse(test.hasNext());
        assertNull(test.next());
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {32, 34, 34, 36, 36, 38, 38, 40, 40, 42, 42, 44, 44, 46, 46, 48, 48, 50, 50, 52, 52, 54, 54, 56, 56, 54, 54, 52, 52, 50, 50, 48, 48, 46, 46, 44, 44, 42, 42, 40, 40, 38, 38, 36, 36, 34, 34, 32};
        Iterator<Integer> test = new HexagonalMask(56, 48).getLineWidth();
        for (int i = 0; i < expected.length; i++) {
            assertTrue("i: " + i, test.hasNext());
            assertEquals("i: " + i, expected[i], (int) test.next());
        }
        assertFalse(test.hasNext());
        assertNull(test.next());
    }
}