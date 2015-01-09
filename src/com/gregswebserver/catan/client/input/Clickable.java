package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.client.event.UserEvent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used in the Hitbox interface to pass user input around.
 */
public interface Clickable {

    public UserEvent onMouseClick(MouseEvent event);

    public UserEvent onKeyTyped(KeyEvent event);

    public UserEvent onMouseScroll(int rot);

    public UserEvent onMouseDrag(Point p);

    public UserEvent onSelect();

    public UserEvent onDeselect();

    public Clickable getClickable(Point p);

}
