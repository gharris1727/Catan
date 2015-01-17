package com.gregswebserver.catan.client.graphics.util;

/**
 * Created by Greg on 1/5/2015.
 * An object that can be animated.
 */
public interface Animated extends Graphical {

    public void step();

    public void reset();

}
