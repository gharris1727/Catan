package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.input.UserEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Clickable {

    private final Point position;
    private final int priority;
    private final int clickableColor;
    private Clickable redirect;

    protected ScreenObject(int priority) {
        this.position = new Point();
        this.priority = priority;
        clickableColor = UniqueColor.getNext();
        redirect = null;
    }

    //Method to defer all clickable operations to another clickable object.
    public ScreenObject setClickable(Clickable redirect) {
        this.redirect = redirect;
        return this;
    }

    //Get render position relative to the parent.
    public final Point getPosition() {
        return position;
    }

    //Set render position relative to the parent.
    public void setPosition(Point position) { this.position.setLocation(position);}

    //Returns the render priority of this object. Used for layering.
    public final int getRenderPriority() {
        return priority;
    }

    //Get this object's clickable color.
    public int getClickableColor() {
        return clickableColor;
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return (redirect == null) ? null : redirect.onMouseClick(event);
    }

    @Override
    public UserEvent onMousePress(MouseEvent event) {
        return (redirect == null) ? null : redirect.onMousePress(event);
    }

    @Override
    public UserEvent onMouseRelease(MouseEvent event) {
        return (redirect == null) ? null : redirect.onMouseRelease(event);
    }

    @Override
    public UserEvent onKeyTyped(KeyEvent event) {
        return (redirect == null) ? null : redirect.onKeyTyped(event);
    }

    @Override
    public UserEvent onKeyPressed(KeyEvent event) {
        return (redirect == null) ? null : redirect.onKeyTyped(event);
    }

    @Override
    public UserEvent onKeyReleased(KeyEvent event) {
        return (redirect == null) ? null : redirect.onKeyReleased(event);
    }

    @Override
    public UserEvent onMouseScroll(MouseWheelEvent event) {
        return (redirect == null) ? null : redirect.onMouseScroll(event);
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        return (redirect == null) ? null : redirect.onMouseDrag(p);
    }

    @Override
    public UserEvent onSelect() {
        return (redirect == null) ? null : redirect.onSelect();
    }

    @Override
    public UserEvent onDeselect() {
        return (redirect == null) ? null : redirect.onDeselect();
    }

    @Override
    public UserEvent onHover() {
        return (redirect == null) ? null : redirect.onHover();
    }

    @Override
    public UserEvent onUnHover() {
        return (redirect == null) ? null : redirect.onUnHover();
    }

    @Override
    public Clickable getClickable(Point p) {
        return (redirect == null) ? this : redirect;
    }

    public abstract String toString();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof ScreenObject) {
            ScreenObject other = (ScreenObject) o;
            return other.clickableColor == this.clickableColor;
        }
        return false;
    }

    public int hashCode() {
        return clickableColor;
    }
}
