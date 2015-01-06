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

    public default UserEvent onMouseClick(MouseEvent event) {
        return null;
    }

    public default UserEvent onKeyTyped(KeyEvent event) {
        return null;
    }

    public default UserEvent onMouseScroll(int wheelRotation) {
        return null;
    }

    public default UserEvent onMouseDrag(Point p) {
        return null;
    }

    public default UserEvent onSelect() {
        return null;
    }

    public default UserEvent onDeselect() {
        return null;
    }

    public default Clickable getClickable(Point p) {
        return this;
    }
}
