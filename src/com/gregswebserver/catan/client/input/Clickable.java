package com.gregswebserver.catan.client.input;

/**
 * Created by Greg on 8/14/2014.
 * An object that can be clicked on.
 * Used in the Hitbox interface to pass user input around.
 */
public interface Clickable {

    //TODO: implement user input via this interface.

    public void onRightClick();

    public void onLeftClick();

    public void onMiddleClick();
}
