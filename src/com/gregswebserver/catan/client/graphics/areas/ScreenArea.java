package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.Resizable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 1/1/2015.
 * A ScreenObject that contains other ScreenObjects.
 */
public abstract class ScreenArea extends ScreenObject implements Iterable<ScreenObject>, Resizable {

    protected Dimension size;
    private Map<Integer, List<ScreenObject>> priorityMap;

    protected ScreenArea(Point position, int priority) {
        super(position, priority);
        clear();
    }

    public void resize(Dimension d) {
        this.size = d;
        graphic = null;
        needsRendering = true;
    }

    public Dimension getSize() {
        return size;
    }

    public void step() {
        for (ScreenObject o : this) o.step();
    }

    public void reset() {
        for (ScreenObject o : this) o.reset();
    }

    protected void clear() {
        priorityMap = new TreeMap<>();
    }

    public void add(ScreenObject object) {
        if (object == null) return;
        List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
        if (objects == null) {
            objects = new LinkedList<>();
            priorityMap.put(object.getRenderPriority(), objects);
        }
        objects.add(object);
        needsRendering = true;
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

    public boolean needsRender() {
        if (needsRendering) return true;
        for (ScreenObject object : this) {
            if (object.needsRender()) return true;
        }
        return false;
    }

    public boolean canRender() {
        return size != null;
    }

    public Graphic getGraphic() {
        if (graphic == null) {
            graphic = new Graphic(size);
            needsRendering = true;
        }
        if (needsRender()) {
            render();
            graphic.clear();
            for (ScreenObject object : this)
                object.getGraphic().renderTo(graphic, null, getObjectPosition(object), object.getHitboxColor());
            needsRendering = false;
        }
        return graphic;
    }

    public abstract Point getObjectPosition(ScreenObject object);

    protected void render() {
    }

}
