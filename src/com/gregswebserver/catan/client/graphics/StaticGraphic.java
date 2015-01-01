package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public class StaticGraphic extends ScreenObject {

    private final Clickable clickable;

    public StaticGraphic(Graphic graphic, Point position, int priority, Clickable clickable) {
        super(position, priority);
        this.graphic = graphic;
        this.clickable = clickable;
    }

    public Clickable getClickable(Point p) {
        return clickable;
    }

    protected void render() {
        //Don't do anything, graphic is already rendered.
    }

    public boolean needsRendering() {
        //Object never needs re-rendering.
        return false;
    }

    public String toString() {
        return super.toString() + " StaticGraphic";
    }
}
