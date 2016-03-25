package com.gregswebserver.catan.client.graphics.screen;

import java.util.Random;

/**
 * Created by Greg on 8/20/2014.
 * Generates a unique color each time it iterates, and never repeats a color.
 */
public class UniqueColor {

    //TODO: evaluate thread safety and likelyhood of collisions.
    private static final Random random = new Random();

    public static int getNext() {
        return random.nextInt(0xffffff);
    }
}
