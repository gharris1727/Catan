package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GridScreenRegion;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A screen region that lives in the bottom corner of the in-game screen.
 */
public class ContextRegion extends GridScreenRegion {

    public ContextRegion(Point position, int priority, RenderMask mask) {
        super(position, priority, mask);
    }

    public void setMask(RenderMask mask) {
        //TODO: resize logic
    }

    protected void render() {

    }

    public String toString() {
        return "ContextScreenArea";
    }
}
