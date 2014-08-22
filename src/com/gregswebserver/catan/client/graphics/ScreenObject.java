package com.gregswebserver.catan.client.graphics;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A set of methods that all object that appear on the screen must implement.
 */
public interface ScreenObject extends Graphical {

    //Get render position relative to the parent.
    public Point getPosition();

    //Get this object's hitbox color.
    public int getHitboxColor();

    //If this object functions as a hitbox, then it will return the sub-object, otherwise it returns it's primary reference.
    public Object getClickable(Point p);

    //Returns the render priority of this object. Used for layering.
    public int getRenderPriority();

    //Recursively searches through children to determine if it needs to be resized.
    public boolean needsRendering();
}
