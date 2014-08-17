package com.gregswebserver.catan.client.renderer;

import java.awt.*;

/**
 * Created by Greg on 8/15/2014.
 * Object stored in the render buffer containing information about it's position.
 * Created by a RenderEvent called to add an object to the buffer.
 */
public class ScreenObject {

    private Renderable renderable;
    private Point position;

    public ScreenObject(Renderable renderable, Point position) {
        this.renderable = renderable;
        this.position = position;
    }

    public Renderable getRenderable() {
        return renderable;
    }

    public Point getPosition() {
        return position;
    }
}
