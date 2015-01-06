package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.graphics.util.UniqueColor;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Graphical, Clickable, Animated {

    private final Point position;
    private final int priority;
    private final int hitboxColor;
    protected Graphic graphic;
    protected boolean needsRendering;

    protected ScreenObject(Point position, int priority) {
        this.position = position;
        this.priority = priority;
        hitboxColor = UniqueColor.getNext();
        needsRendering = true;
    }

    public void step() {
        if (graphic instanceof Animated) ((Animated) graphic).step();
    }

    public void reset() {
        if (graphic instanceof Animated) ((Animated) graphic).reset();
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

    //Forces this object to re-render the next time getGraphic is called.
    public void forceRender() {
        needsRendering = true;
    }

    //Tag to check if this object needs re-rendering.
    public abstract boolean needsRender();

    //Returns whether or not getGraphic() (Graphical) will complete correctly.
    public abstract boolean canRender();
}
