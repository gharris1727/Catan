package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;

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

    public AnimatedObject(String name, int priority, boolean loop) {
        super(name, priority);
        this.loop = loop;
        frames = new LinkedList<>();
        reset();
    }

    public AnimatedObject add(Graphic g) {
        frames.add(g);
        return this;
    }

    @Override
    public void reset() {
        it = frames.iterator();
        needsRendering = true;
    }

    @Override
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

    @Override
    public boolean needsRender() {
        return needsRendering;
    }

    @Override
    public Graphic getGraphic() {
        needsRendering = false;
        if (current == null)
            throw new NotYetRenderableException("No graphic assigned to animation.");
        return current;
    }
}
