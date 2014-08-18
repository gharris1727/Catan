package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.masks.RenderMask;
import com.gregswebserver.catan.client.renderer.Renderable;

import java.awt.*;

/**
 * Created by Greg on 8/15/2014.
 * Object stored in the render buffer containing information about it's position.
 * Created by a RenderEvent called to add an object to the buffer.
 */
public class ScreenObject {

    private Renderable renderable;
    private RenderMask mask;
    private Point position;

    public ScreenObject(Renderable renderable, RenderMask mask, Point position) {
        this.renderable = renderable;
        this.mask = mask;
        this.position = position;
    }

    public Renderable getRenderable() {
        return renderable;
    }

    public RenderMask getMask() {
        return mask;
    }

    public Point getPosition() {
        return position;
    }
}
