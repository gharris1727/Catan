package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.graphics.util.UniqueColor;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Graphical, Clickable {

    private final Point position;
    private final int priority;
    private final int hitboxColor;

    protected ScreenObject(Point position, int priority) {
        this.position = position;
        this.priority = priority;
        hitboxColor = UniqueColor.getNext();
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

    //Tag to check if the ScreenObject can be animated using step().
    public abstract boolean isAnimated();

    //Tag to check if this object needs re-rendering.
    public abstract boolean needsRender();

    //Returns whether or not getGraphic() (Graphical) will complete correctly.
    public abstract boolean canRender();

    //Used in debugging the Clickable portion of these objects.
    public abstract String toString();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ScreenObject) {
            ScreenObject other = (ScreenObject) o;
            return other.hitboxColor == this.hitboxColor;
        }
        return false;
    }

    public int hashCode() {
        return hitboxColor;
    }
}
