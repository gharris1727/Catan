package com.gregswebserver.catan.client.masks;

import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class HexagonalMaskTest {

    private final Dimension testSize = new Dimension(56, 48);

    @Test
    public void testGetLeftPadding() throws Exception {
        int[] expected = {12, 11, 11, 10, 10, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10, 11, 11, 12};
        ArrayList<Integer> test = new HexagonalMask(testSize).getLeftPadding();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int) test.get(i));
        }
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[] expected = {32, 34, 34, 36, 36, 38, 38, 40, 40, 42, 42, 44, 44, 46, 46, 48, 48, 50, 50, 52, 52, 54, 54, 56, 56, 54, 54, 52, 52, 50, 50, 48, 48, 46, 46, 44, 44, 42, 42, 40, 40, 38, 38, 36, 36, 34, 34, 32};
        ArrayList<Integer> test = new HexagonalMask(testSize).getLineWidth();
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], (int) test.get(i));
        }
    }
}