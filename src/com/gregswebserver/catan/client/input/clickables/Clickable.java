package com.gregswebserver.catan.client.input.clickables;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used in the Hitbox interface to pass user input around.
 */
public interface Clickable {

    public default void onMouseClick(int button) {
    }

    public default void onKeyTyped(int key) {
    }

    public default void onMouseScroll(int wheelRotation) {
    }

    public default void onMouseDrag(Point p) {
    }
}
