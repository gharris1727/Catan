package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Graphical;
import com.gregswebserver.catan.client.graphics.util.Resizable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 1/1/2015.
 * A ScreenObject that contains other ScreenObjects.
 */
public abstract class ScreenRegion extends ScreenObject implements Iterable<ScreenObject>, Resizable, Graphical, Animated {

    private RenderMask mask;
    private Graphic graphic;
    private boolean needsRendering;
    private Map<Integer, List<ScreenObject>> priorityMap;

    public ScreenRegion(Point position, int priority, RenderMask mask) {
        super(position, priority);
        setMask(mask);
        clear();
    }

    protected ScreenRegion(Point position, int priority) {
        super(position, priority);
    }

    public final RenderMask getMask() {
        return mask;
    }

    public void setMask(RenderMask mask) {
        this.mask = mask;
        graphic = new Graphic(mask);
        forceRender();
    }

    public final void forceRender() {
        needsRendering = true;
    }

    protected void clear() {
        priorityMap = new TreeMap<>();
    }

    public ScreenObject add(ScreenObject object) {
        if (object == null) return null;
        List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
        if (objects == null) {
            objects = new LinkedList<>();
            priorityMap.put(object.getRenderPriority(), objects);
        }
        objects.add(object);
        forceRender();
        return object;
    }

    public ScreenObject remove(ScreenObject object) {
        if (object == null) return null;
        List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
        if (objects == null)
            return object;
        objects.remove(object);
        forceRender();
        return object;
    }

    public final Iterator<ScreenObject> iterator() {
        return new Iterator<ScreenObject>() {

            private Iterator<List<ScreenObject>> treeIterator = priorityMap.values().iterator();
            private Iterator<ScreenObject> listIterator;

            public boolean hasNext() {
                return treeIterator.hasNext() || (listIterator != null && listIterator.hasNext());
            }

            public ScreenObject next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if (listIterator == null || !listIterator.hasNext())
                    listIterator = treeIterator.next().iterator();
                return listIterator.next();
            }
        };
    }

    public final boolean isAnimated() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                return true;
        return false;
    }

    public final void step() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).step();
    }

    public final void reset() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).reset();
    }

    public final boolean needsRender() {
        if (needsRendering) return true;
        for (ScreenObject object : this)
            if (object.needsRender())
                return true;
        return false;
    }

    public final boolean isGraphical() {
        return true;
    }

    public Graphic getGraphic() {
        if (needsRender()) {
            if (graphic == null)
                throw new IllegalStateException("Screen Region " + this + " has no size.");
            render();
            graphic.clear();
            for (ScreenObject object : this)
                if (object.isGraphical())
                    ((Graphical) object).getGraphic().renderTo(graphic, getObjectPosition(object), object.getHitboxColor());
            needsRendering = false;
        }
        return graphic;
    }

    protected void render() {
    }

    protected Point getObjectPosition(ScreenObject object) {
        return object.getPosition();
    }

}
