package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.util.UniqueColor;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Graphical {

    protected final Point position;
    private final int priority;
    private final int color;
    protected Graphic graphic;
    protected boolean needsRendering;

    protected ScreenObject(Point position, int priority) {
        this.position = position;
        this.priority = priority;
        color = UniqueColor.getNext();
        needsRendering = true;
    }

    //Get render position relative to the parent.
    public Point getPosition() {
        return position;
    }

    //Get this object's hitbox color.
    public int getHitboxColor() {
        return color;
    }

    //If this object functions as a hitbox, then it will return the sub-object, otherwise it returns it's primary reference.
    public abstract Clickable getClickable(Point p);

    //Renders the graphic, and ensures that graphic is not null.
    protected abstract void render();

    //Returns the render priority of this object. Used for layering.
    public int getRenderPriority() {
        return priority;
    }

    //Recursively searches through children to determine if it needs to be resized.
    public boolean needsRendering() {
        return needsRendering;
    }

    public void forceRender() {
        needsRendering = true;
    }

    public Graphic getGraphic() {
        if (graphic == null || needsRendering()) {
            render();
            needsRendering = false;
        }
        return graphic;
    }
}
