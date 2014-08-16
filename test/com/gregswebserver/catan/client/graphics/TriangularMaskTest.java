package com.gregswebserver.catan.client.graphics;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class TriangularMaskTest {

    @Test
    public void testGetWidth() throws Exception {
        int[] expected = {0, 2, 2, 3, 3, 5, 5, 6, 6, 8, 8, 9, 9, 11, 11, 12, 12, 14, 14};
        for (int i = 0; i < expected.length; i++) {
            TriangularMask test = new TriangularMask(i);
            assertEquals("i: " + i, expected[i], test.getWidth());
        }
    }

    @Test
    public void testGetLeftPadding() throws Exception {
        int[][] expected = {
                {},
                {0},
                {0, 0},
                {2, 0, 2},
                {2, 0, 0, 2},
                {3, 2, 0, 2, 3},
                {3, 2, 0, 0, 2, 3},
                {5, 3, 2, 0, 2, 3, 5},
                {5, 3, 2, 0, 0, 2, 3, 5},
                {6, 5, 3, 2, 0, 2, 3, 5, 6},
                {6, 5, 3, 2, 0, 0, 2, 3, 5, 6},
                {8, 6, 5, 3, 2, 0, 2, 3, 5, 6, 8},
                {8, 6, 5, 3, 2, 0, 0, 2, 3, 5, 6, 8},
                {9, 8, 6, 5, 3, 2, 0, 2, 3, 5, 6, 8, 9},
                {9, 8, 6, 5, 3, 2, 0, 0, 2, 3, 5, 6, 8, 9},
                {11, 9, 8, 6, 5, 3, 2, 0, 2, 3, 5, 6, 8, 9, 11},
                {11, 9, 8, 6, 5, 3, 2, 0, 0, 2, 3, 5, 6, 8, 9, 11},
                {12, 11, 9, 8, 6, 5, 3, 2, 0, 2, 3, 5, 6, 8, 9, 11, 12}
        };
        for (int i = 0; i < expected.length; i++) {
            ArrayList<Integer> test = new TriangularMask(i).getLeftPadding();
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], (int) test.get(j));
            }
        }
    }


    @Test
    public void testGetLineLength() throws Exception {
        int[][] expected = {
                {},
                {2},
                {2, 2},
                {1, 3, 1},
                {1, 3, 3, 1},
                {2, 3, 5, 3, 2},
                {2, 3, 5, 5, 3, 2},
                {1, 3, 4, 6, 4, 3, 1},
                {1, 3, 4, 6, 6, 4, 3, 1},
                {2, 3, 5, 6, 8, 6, 5, 3, 2},
                {2, 3, 5, 6, 8, 8, 6, 5, 3, 2},
                {1, 3, 4, 6, 7, 9, 7, 6, 4, 3, 1},
                {1, 3, 4, 6, 7, 9, 9, 7, 6, 4, 3, 1},
                {2, 3, 5, 6, 8, 9, 11, 9, 8, 6, 5, 3, 2},
                {2, 3, 5, 6, 8, 9, 11, 11, 9, 8, 6, 5, 3, 2},
                {1, 3, 4, 6, 7, 9, 10, 12, 10, 9, 7, 6, 4, 3, 1},
                {1, 3, 4, 6, 7, 9, 10, 12, 12, 10, 9, 7, 6, 4, 3, 1},
                {2, 3, 5, 6, 8, 9, 11, 12, 14, 12, 11, 9, 8, 6, 5, 3, 2}
        };
        for (int i = 0; i < expected.length; i++) {
            ArrayList<Integer> test = new TriangularMask(i).getLineWidth();
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], (int) test.get(j));
            }
        }
    }
}