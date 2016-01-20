package com.gregswebserver.catan.client.graphics.util;

import com.gregswebserver.catan.client.graphics.graphics.Graphical;

/**
 * Created by Greg on 1/5/2015.
 * An object that can be animated.
 */
public interface Animated extends Graphical {

    void step();

    void reset();

}
