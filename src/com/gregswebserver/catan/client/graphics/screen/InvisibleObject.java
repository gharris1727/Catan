package com.gregswebserver.catan.client.graphics.screen;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * An object that is
 */
public abstract class InvisibleObject extends ScreenObject {

    protected InvisibleObject(Point position, int priority) {
        super(position, priority);
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public boolean isGraphical() {
        return false;
    }

    @Override
    public boolean needsRender() {
        return false;
    }
}
