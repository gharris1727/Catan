package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.renderer.Renderable;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Greg on 8/16/2014.
 * A sequence of graphics for displaying an animated feature.
 */
public class Animation implements Renderable {

    ArrayList<Graphic> graphics;
    int currentFrame = 0;
    boolean loop = false;

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

    public boolean done() {
        return (!loop && currentFrame >= graphics.size());
    }

    public void renderTo(Graphic to, Point toPos) {
        Graphic g = graphics.get(currentFrame);
        step();
        if (currentFrame >= graphics.size()) {
            if (loop) reset();
            else return; //Animation is done, don't print to the screen anymore.
        }
        g.renderTo(to, toPos);
    }
}
