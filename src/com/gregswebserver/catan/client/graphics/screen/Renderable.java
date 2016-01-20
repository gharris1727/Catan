package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphical;

/**
 * Created by greg on 1/15/16.
 * Abstraction for some of the features relevant to rendering an object to the screen.
 */
public interface Renderable extends Graphical {

    void forceRender();

    boolean isRenderable();
}
