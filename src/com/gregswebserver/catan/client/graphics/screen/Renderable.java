package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.common.profiler.TimeSlice;

/**
 * Created by greg on 1/15/16.
 * A Graphical object where the production of a Graphic may be non-trivial
 * This may be because it depends on outside resources, or other Graphical objects.
 */
public interface Renderable extends Graphical {

    void forceRender();

    boolean isRenderable();

    void assertRenderable();

    default TimeSlice getRenderTime() {
        return null;
    }
}
