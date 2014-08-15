package com.gregswebserver.catan.client.input;

import com.gregswebserver.catan.event.GenericEvent;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used in the Hitbox interface to pass user input around.
 */
public interface Clickable {

    public GenericEvent onRightClick();

    public GenericEvent onLeftClick();
}
