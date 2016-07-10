package catan.client.graphics.screen;

import catan.client.graphics.graphics.Graphic;
import catan.common.profiler.TimeSlice;

/**
 * Created by greg on 1/15/16.
 * An object that has a graphic object attached.
 */
public interface Graphical {

    void forceRender();

    boolean needsRender();

    boolean isRenderable();

    void assertRenderable();

    Graphic getGraphic();

    default TimeSlice getRenderTime() {
        return null;
    }
}
