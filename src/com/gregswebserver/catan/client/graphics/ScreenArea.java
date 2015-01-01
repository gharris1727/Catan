package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Greg on 8/19/2014.
 * A screen object consisting of other screen objects with HitboxColors to differentiate them.
 */
public class ScreenArea extends ScreenObject {

    protected Dimension size;
    protected Map<Integer, ScreenObject> hitboxMap;
    protected Map<Integer, List<ScreenObject>> priorityMap;

    public ScreenArea(Dimension size, Point position, int priority) {
        super(position, priority);
        clear();
        resize(size);
    }

    public Clickable getClickable(Point p) {
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        int color = getGraphic().getHitboxColor(subPosition);
        ScreenObject object = hitboxMap.get(color);
        if (object == null) return null;
        return object.getClickable(p);
    }

    public boolean needsRendering() {
        if (needsRendering) return true;
        for (ScreenObject object : hitboxMap.values()) {
            if (object.needsRendering()) return true;
        }
        return false;
    }

    protected void preRender() {
        if (graphic == null) {
            graphic = new Graphic(size);
        } else {
            graphic.clear();
        }
    }

    protected void render() {
        preRender();
        postRender();
    }

    protected void postRender() {
        for (List<ScreenObject> objects : priorityMap.values()) {
            for (ScreenObject object : objects) {
                object.getGraphic().renderTo(graphic, null, object.getPosition(), object.getHitboxColor());
            }
        }
    }

    public void addScreenObject(ScreenObject object) {
        hitboxMap.put(object.getHitboxColor(), object);
        List<ScreenObject> objects = priorityMap.get(object.getRenderPriority());
        if (objects == null) {
            objects = new LinkedList<>();
            priorityMap.put(object.getRenderPriority(), objects);
        }
        objects.add(object);
        needsRendering = true;
    }

    public void clear() {
        hitboxMap = new HashMap<>();
        priorityMap = new TreeMap<>();
    }

    public void resize(Dimension d) {
        this.size = d;
        graphic = null;
        needsRendering = true;
    }

    public Dimension getSize() {
        return size;
    }

    public String toString() {
        return "ScreenArea";
    }
}
