package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.util.UniqueColor;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Clickable {

    private final Point position;
    private final int priority;
    private final int hitboxColor;
    private Clickable redirect;

    protected ScreenObject(Point position, int priority) {
        this.position = position;
        this.priority = priority;
        hitboxColor = UniqueColor.getNext();
        redirect = null;
    }

    //Method to defer all clickable operations to another clickable object.
    public void setClickable(Clickable redirect) {
        this.redirect = redirect;
    }

    //Get render position relative to the parent.
    public final Point getPosition() {
        return position;
    }

    //Returns the render priority of this object. Used for layering.
    public final int getRenderPriority() {
        return priority;
    }

    //Get this object's hitbox color.
    public int getHitboxColor() {
        return hitboxColor;
    }

    public UserEvent onMouseClick(MouseEvent event) {
        return (redirect == null) ? null : redirect.onMouseClick(event);
    }

    public UserEvent onKeyTyped(KeyEvent event) {
        return (redirect == null) ? null : redirect.onKeyTyped(event);
    }

    public UserEvent onMouseScroll(int rot) {
        return (redirect == null) ? null : redirect.onMouseScroll(rot);
    }

    public UserEvent onMouseDrag(Point p) {
        return (redirect == null) ? null : redirect.onMouseDrag(p);
    }

    public UserEvent onSelect() {
        return (redirect == null) ? null : redirect.onSelect();
    }

    public UserEvent onDeselect() {
        return (redirect == null) ? null : redirect.onDeselect();
    }

    public Clickable getClickable(Point p) {
        return (redirect == null) ? this : redirect;
    }

    //Tag to check if the ScreenObject can be rendered.
    public abstract boolean isGraphical();

    //Tag to check if the ScreenObject can be animated using step().
    public abstract boolean isAnimated();

    //Tag to check if this object needs re-rendering.
    public abstract boolean needsRender();

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