package com.gregswebserver.catan.common.util;

import java.util.Random;

/**
 * Created by Greg on 8/20/2014.
 * Generates a unique color each time it iterates, and never repeats a color.
 */
public class UniqueColor {

    private static Random random = new Random();

    public static int getNext() {
        return random.nextInt(0xffffff);
    }
}
