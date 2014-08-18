package com.gregswebserver.catan.client.hitbox;

import java.awt.*;

/**
 * Created by Greg on 8/16/2014.
 * Converts a screen coordinate into an object that can be interacted with.
 */
public interface Hitbox {

    public Object getObject(Point p);
}
