package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public abstract class StaticObject extends ScreenObject implements Graphical {

    private final Graphic graphic;

    protected StaticObject(int priority, Graphic graphic) {
        super(new Point(), priority);
        this.graphic = graphic;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public boolean isGraphical() {
        return true;
    }

    @Override
    public boolean needsRender() {
        return false;
    }

    @Override
    public Graphic getGraphic() {
        return graphic;
    }

}
