package com.gregswebserver.catan.graphics;

/**
 * Created by Greg on 8/13/2014.
 * Sprite optimized to be hexagonal, and allow for 60-degree rotations.
 */
public class Sprite {

    private RenderMask mask;
    private int[][] pixels;

    public Sprite(RenderMask mask, SpriteSheet sheet) {

    }

    public RenderMask getMask() {
        return mask;
    }
}
