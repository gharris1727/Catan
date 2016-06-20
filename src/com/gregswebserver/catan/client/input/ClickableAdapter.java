package com.gregswebserver.catan.client.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by greg on 2/6/16.
 * A clickable that does nothing by default
 */
public abstract class ClickableAdapter implements Clickable {
    @Override
    public void onMouseClick(UserEventListener listener, MouseEvent event) { }

    @Override
    public void onMousePress(UserEventListener listener, MouseEvent e) { }

    @Override
    public void onMouseRelease(UserEventListener listener, MouseEvent e) { }

    @Override
    public void onKeyTyped(UserEventListener listener, KeyEvent event) { }

    @Override
    public void onKeyPressed(UserEventListener listener, KeyEvent e) { }

    @Override
    public void onKeyReleased(UserEventListener listener, KeyEvent e) { }

    @Override
    public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) { }

    @Override
    public void onMouseDrag(UserEventListener listener, Point p) { }

    @Override
    public void onHover(UserEventListener listener) { }

    @Override
    public void onUnHover(UserEventListener listener) { }

    @Override
    public void onLinger(UserEventListener listener) { }

    @Override
    public void onSelect(UserEventListener listener) { }

    @Override
    public void onDeselect(UserEventListener listener) { }

    @Override
    public Clickable getClickable(Point p) {
        return this;
    }
}
