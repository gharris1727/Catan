package com.gregswebserver.catan.client.input;

/**
 * Created by Greg on 8/13/2014.
 * Hitbox object for storing what object rendered what on the screen.
 */
public interface ScreenHitbox {

    //TODO: evaluate usefulness of this interface.

    public void setGlobalSize(int x, int y);

    public void setViewSize(int x, int y);

    public void setViewLocation(int x, int y);

    public void setScreenLocation(int x, int y);

    public Clickable getObjectAtPoint(int x, int y);

}
