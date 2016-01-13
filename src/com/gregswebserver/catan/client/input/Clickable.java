package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.event.UserEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used to identify the screen object that the client clicked on.
 */
public interface Clickable {

    UserEvent onMouseClick(MouseEvent event);

    UserEvent onKeyTyped(KeyEvent event);

    UserEvent onMouseScroll(int rot);

    UserEvent onMouseDrag(Point p);

    UserEvent onSelect();

    UserEvent onDeselect();

    Clickable getClickable(Point p);

}
