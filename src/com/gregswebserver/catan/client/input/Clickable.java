package com.gregswebserver.catan.client.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used to identify the screen object that the client clicked on.
 */
public interface Clickable {

    void onMouseClick(UserEventListener listener, MouseEvent event);

    void onMousePress(UserEventListener listener, MouseEvent event);

    void onMouseRelease(UserEventListener listener, MouseEvent event);

    void onKeyTyped(UserEventListener listener, KeyEvent event);

    void onKeyPressed(UserEventListener listener, KeyEvent event);

    void onKeyReleased(UserEventListener listener, KeyEvent event);

    void onMouseScroll(UserEventListener listener, MouseWheelEvent event);

    void onMouseDrag(UserEventListener listener, Point p);

    void onHover(UserEventListener listener);

    void onUnHover(UserEventListener listener);

    void onLinger(UserEventListener listener);

    void onSelect(UserEventListener listener);

    void onDeselect(UserEventListener listener);

    Clickable getClickable(Point p);
}
