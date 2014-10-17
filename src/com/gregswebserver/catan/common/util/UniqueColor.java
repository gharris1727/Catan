package com.gregswebserver.catan.common.util;

/**
 * Created by Greg on 8/20/2014.
 * Generates a unique color each time it iterates, and never repeats a color.
 */
public class UniqueColor {

    private static int next = 2;

    public static int getNext() {
        return next++;
    }
}
