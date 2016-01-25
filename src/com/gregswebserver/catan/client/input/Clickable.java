package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.event.UserEvent;

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

    UserEvent onMouseClick(MouseEvent event);

    UserEvent onMousePress(MouseEvent event);

    UserEvent onMouseRelease(MouseEvent event);

    UserEvent onKeyTyped(KeyEvent event);

    UserEvent onKeyPressed(KeyEvent event);

    UserEvent onKeyReleased(KeyEvent event);

    UserEvent onMouseScroll(MouseWheelEvent event);

    UserEvent onMouseDrag(Point p);

    UserEvent onHover();

    UserEvent onUnHover();

    UserEvent onSelect();

    UserEvent onDeselect();

    Clickable getClickable(Point p);
}
