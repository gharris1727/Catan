package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.graphics.Graphic;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/15/2014.
 * Object stored in the render buffer containing information about it's position.
 * Created by a RenderEvent called to add an object to the buffer.
 */
public class ScreenObject {

    private Graphic graphic;
    private Point position;
    private Clickable object;

    public ScreenObject(Graphic graphic, Point position, Clickable object) {
        this.graphic = graphic;
        this.position = position;
        this.object = object;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public Point getPosition() {
        return position;
    }

    public Clickable getObject() {
        return object;
    }
}
