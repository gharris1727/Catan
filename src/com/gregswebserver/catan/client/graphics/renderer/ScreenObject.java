package com.gregswebserver.catan.client.graphics.renderer;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.graphics.util.UniqueColor;
import com.gregswebserver.catan.client.input.clickables.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Graphical {

    protected final Point position;
    protected final Clickable clickable;
    private final int priority;
    private final int hitboxColor;
    protected Graphic graphic;
    protected boolean needsRendering;

    protected ScreenObject(Point position, int priority, Clickable clickable) {
        this.position = position;
        this.priority = priority;
        this.clickable = clickable;
        hitboxColor = UniqueColor.getNext();
        needsRendering = true;
    }

    //Get render position relative to the parent.
    public Point getPosition() {
        return position;
    }

    //Get this object's hitbox color.
    public int getHitboxColor() {
        return hitboxColor;
    }

    //Returns the render priority of this object. Used for layering.
    public int getRenderPriority() {
        return priority;
    }

    //If this object functions as a hitbox, then it will return the sub-object, otherwise it returns it's primary reference.
    public Clickable getClickable(Point p) {
        return clickable;
    }

    //Tag to check if this object needs re-rendering.
    public boolean needsRendering() {
        return graphic == null || needsRendering;
    }

    //Forces this object to re-render the next time getGraphic is called.
    public void forceRender() {
        needsRendering = true;
    }
}
