package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Resizable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 1/1/2015.
 * A ScreenObject that contains other ScreenObjects.
 */
public abstract class ObjectArea extends ScreenObject implements Iterable<ScreenObject>, Resizable, Animated {

    private Dimension size;
    private Graphic graphic;
    private boolean needsRendering;
    private Map<Integer, List<ScreenObject>> priorityMap;

    protected ObjectArea(Point position, int priority) {
        super(position, priority);
        clear();
    }

    public Dimension getSize() {
        return size;
    }

    public void setSize(Dimension d) {
        this.size = d;
        graphic = new Graphic(size);
        needsRendering = true;
    }

    public void forceRender() {
        needsRendering = true;
    }

    public void step() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).step();
    }

    public void reset() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                ((Animated) object).reset();
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
        needsRendering = true;
        return object;
    }

    public ScreenObject remove(ScreenObject object) {
        if (object == null) return null;
        List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
        if (objects == null) {
            return object;
        }
        objects.remove(object);
        needsRendering = true;
        return object;
    }

    public Iterator<ScreenObject> iterator() {
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

    public boolean isAnimated() {
        for (ScreenObject object : this)
            if (object.isAnimated())
                return true;
        return false;
    }

    public boolean needsRender() {
        if (needsRendering) return true;
        for (ScreenObject object : this)
            if (object.needsRender() && object.canRender())
                return true;
        return false;
    }

    public boolean canRender() {
        return size != null && graphic != null;
    }

    public Graphic getGraphic() {
        if (!canRender())
            throw new IllegalStateException("Cannot render an ObjectArea without a defined size.");
        if (needsRender()) {
            render();
            graphic.clear();
            for (ScreenObject object : this)
                if (object.canRender())
                    object.getGraphic().renderTo(graphic, getObjectPosition(object), object.getHitboxColor());
            needsRendering = false;
        }
        return graphic;
    }

    protected void render() {
    }

    public abstract Point getObjectPosition(ScreenObject object);


}
