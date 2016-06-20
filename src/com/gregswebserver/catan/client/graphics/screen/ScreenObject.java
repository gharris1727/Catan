package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.input.UserEventListener;
import com.gregswebserver.catan.client.renderer.RenderThread;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public abstract class ScreenObject implements Clickable, Graphical {

    private final Point position;
    private final String name;
    private final int priority;
    private final int clickableColor;
    private Clickable redirect;
    private RenderThread renderer;

    protected ScreenObject(String name, int priority) {
        this.position = new Point();
        this.name = name;
        this.priority = priority;
        clickableColor = UniqueColor.getNext();
        redirect = null;
    }

    //Method to defer all clickable operations to another clickable object.
    public ScreenObject setClickable(Clickable redirect) {
        this.redirect = redirect;
        return this;
    }

    public RenderThread getRenderer() {
        return renderer;
    }

    public void setRenderer(RenderThread renderer) {
        this.renderer = renderer;
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
    public void onMouseClick(UserEventListener listener, MouseEvent event) {
        if (redirect != null) redirect.onMouseClick(listener, event);
    }

    @Override
    public void onMousePress(UserEventListener listener, MouseEvent event) {
        if (redirect != null) redirect.onMousePress(listener, event);
    }

    @Override
    public void onMouseRelease(UserEventListener listener, MouseEvent event) {
        if (redirect != null) redirect.onMouseRelease(listener, event);
    }

    @Override
    public void onKeyTyped(UserEventListener listener, KeyEvent event) {
        if (redirect != null) redirect.onKeyTyped(listener, event);
    }

    @Override
    public void onKeyPressed(UserEventListener listener, KeyEvent event) {
        if (redirect != null) redirect.onKeyPressed(listener, event);
    }

    @Override
    public void onKeyReleased(UserEventListener listener, KeyEvent event) {
        if (redirect != null) redirect.onKeyReleased(listener, event);
    }

    @Override
    public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
        if (redirect != null) redirect.onMouseScroll(listener, event);
    }

    @Override
    public void onMouseDrag(UserEventListener listener, Point p) {
        if (redirect != null) redirect.onMouseDrag(listener, p);
    }

    @Override
    public void onSelect(UserEventListener listener) {
        if (redirect != null) redirect.onSelect(listener);
    }

    @Override
    public void onDeselect(UserEventListener listener) {
        if (redirect != null) redirect.onDeselect(listener);
    }

    @Override
    public void onHover(UserEventListener listener) {
        if (redirect != null) redirect.onHover(listener);
    }

    @Override
    public void onUnHover(UserEventListener listener) {
        if (redirect != null) redirect.onUnHover(listener);
    }

    @Override
    public void onLinger(UserEventListener listener) {
        if (redirect != null) redirect.onLinger(listener);
    }

    @Override
    public Clickable getClickable(Point p) {
        return (redirect == null) ? this : redirect;
    }

    public final String toString() {
        return name;
    }
}
