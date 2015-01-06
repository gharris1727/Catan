package com.gregswebserver.catan.client.graphics.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Greg on 8/16/2014.
 * A sequence of graphics for displaying an animated feature.
 */
public class Animation extends Graphic implements Animated {

    private ArrayList<Graphic> graphics;
    private int currentFrame = 0;
    private boolean loop = false;

    public Animation(boolean loop) {
        this.loop = loop;
    }

    public Animation add(Graphic g) {
        graphics.add(g);
        return this;
    }

    public void reset() {
        currentFrame = 0;
    }

    public void step() {
        currentFrame++;
    }

    public void renderTo(Graphic to, RenderMask toMask, Point toPos, int color) {
        if (currentFrame >= graphics.size()) {
            if (loop) reset();
            else return; //Animation is done, don't print to the screen anymore.
        }
        Graphic g = graphics.get(currentFrame);
        step();
        g.renderTo(to, toMask, toPos, color);
    }
}
