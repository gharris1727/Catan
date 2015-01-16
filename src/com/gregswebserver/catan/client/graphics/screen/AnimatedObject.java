package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.graphics.util.Graphic;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 1/6/2015.
 * A graphic that contains some animation.
 */
public abstract class AnimatedObject extends ScreenObject implements Animated {

    private final boolean loop;
    private final List<Graphic> frames;
    private Iterator<Graphic> it;
    private Graphic current;
    private boolean needsRendering;

    public AnimatedObject(Point position, int priority, boolean loop) {
        super(position, priority);
        this.loop = loop;
        frames = new LinkedList<>();
        reset();
    }

    public AnimatedObject add(Graphic g) {
        frames.add(g);
        return this;
    }

    public void reset() {
        it = frames.iterator();
        needsRendering = true;
    }

    public void step() {
        if (frames.size() == 0) return;
        if (it.hasNext())
            current = it.next();
        else {
            if (loop) {
                reset();
                step();
            } else
                current = null;
        }
        needsRendering = true;
    }

    public boolean isAnimated() {
        return true;
    }

    public boolean needsRender() {
        return needsRendering && canRender();
    }

    public Graphic getGraphic() {
        needsRendering = false;
        return current;
    }
}
