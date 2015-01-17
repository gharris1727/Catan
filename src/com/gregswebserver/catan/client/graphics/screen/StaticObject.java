package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public abstract class StaticObject extends ScreenObject implements Graphical {

    private final Graphic graphic;

    protected StaticObject(Point position, int priority, Graphic graphic) {
        super(position, priority);
        this.graphic = graphic;
    }

    public boolean isAnimated() {
        return false;
    }

    public boolean isGraphical() {
        return true;
    }

    public boolean needsRender() {
        return false;
    }

    public Graphic getGraphic() {
        return graphic;
    }

}
