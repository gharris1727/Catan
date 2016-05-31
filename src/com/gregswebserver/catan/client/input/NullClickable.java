package com.gregswebserver.catan.client.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by greg on 2/6/16.
 * A clickable that does nothing.
 */
public class NullClickable implements Clickable {
    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return null;
    }

    @Override
    public UserEvent onMousePress(MouseEvent e) {
        return null;
    }

    @Override
    public UserEvent onMouseRelease(MouseEvent e) {
        return null;
    }

    @Override
    public UserEvent onKeyTyped(KeyEvent event) {
        return null;
    }

    @Override
    public UserEvent onKeyPressed(KeyEvent e) {
        return null;
    }

    @Override
    public UserEvent onKeyReleased(KeyEvent e) {
        return null;
    }

    @Override
    public UserEvent onMouseScroll(MouseWheelEvent event) {
        return null;
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        return null;
    }

    @Override
    public UserEvent onHover() {
        return null;
    }

    @Override
    public UserEvent onUnHover() {
        return null;
    }

    @Override
    public UserEvent onLinger() {
        return null;
    }

    @Override
    public UserEvent onSelect() {
        return null;
    }

    @Override
    public UserEvent onDeselect() {
        return null;
    }

    @Override
    public Clickable getClickable(Point p) {
        return this;
    }

    public String toString() {
        return "NullClickable";
    }
}
