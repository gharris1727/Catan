package com.gregswebserver.catan.client.graphics.graphics;

import com.sun.istack.internal.NotNull;

/**
 * Created by Greg on 8/21/2014.
 * An object that has a graphics object attached.
 */
public interface Graphical {

    @NotNull
    Graphic getGraphic();
}
