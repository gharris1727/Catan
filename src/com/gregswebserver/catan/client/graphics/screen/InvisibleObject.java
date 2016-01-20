package com.gregswebserver.catan.client.graphics.screen;

/**
 * Created by Greg on 1/16/2015.
 * An object that is
 */
public abstract class InvisibleObject extends ScreenObject {

    protected InvisibleObject(int priority) {
        super(priority);
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
