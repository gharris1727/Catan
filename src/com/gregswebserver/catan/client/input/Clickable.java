package com.gregswebserver.catan.client.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used in the Hitbox interface to pass user input around.
 */
public interface Clickable {

    public default void onMouseClick(MouseEvent event) {
    }

    public default void onKeyTyped(KeyEvent event) {
    }

    public default void onMouseScroll(int wheelRotation) {
    }

    public default void onMouseDrag(Point p) {
    }

    public default void onSelect() {
    }

    public default void onDeselect() {
    }
}
