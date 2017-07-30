package catan.client.graphics.screen;

import java.util.Random;

/**
 * Created by Greg on 8/20/2014.
 * Generates a unique color each time it iterates, and never repeats a color.
 */
public final class UniqueColor {

    //TODO: evaluate thread safety and likelihood of collisions.
    private static final Random random = new Random();

    private UniqueColor() {
    }

    public static int getNext() {
        return random.nextInt(0xffffff);
    }
}
