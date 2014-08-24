package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Greg on 8/19/2014.
 * A screen object consisting of other screen objects with HitboxColors to differentiate them.
 */
public class ScreenArea extends ScreenObject {

    protected Dimension size;
    private Point viewPoint;
    private HashMap<Integer, ScreenObject> subObjects;
    private TreeMap<Integer, ArrayList<ScreenObject>> renderPriority;

    public ScreenArea(Dimension size, Point position, Point viewPoint, int priority) {
        super(position, priority);
        this.viewPoint = viewPoint;
        clear();
        resize(size);
    }

    public Clickable getClickable(Point p) {
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        int color = getGraphic().getHitboxColor(subPosition);
        ScreenObject object = subObjects.get(color);
        if (object == null) return null;
        return object.getClickable(p);
    }

    public boolean needsRendering() {
        if (needsRendering) return true;
        for (ScreenObject object : subObjects.values()) {
            if (object.needsRendering()) return true;
        }
        return false;
    }

    public void render() {
        if (graphic == null) {
            graphic = new Graphic(size);
        } else {
            graphic.clear();
        }
        for (ScreenObject object : subObjects.values()) {
            Point position = object.getPosition();
            Point renderPosition = new Point(position.x - viewPoint.x, position.y - viewPoint.y);
            object.getGraphic().renderTo(graphic, null, renderPosition, object.getHitboxColor());
        }
    }

    public void addScreenObject(ScreenObject object) {
        subObjects.put(object.getHitboxColor(), object);
        ArrayList<ScreenObject> objects = renderPriority.get(object.getRenderPriority());
        if (objects == null) {
            objects = new ArrayList<>();
            renderPriority.put(object.getRenderPriority(), objects);
        }
        objects.add(object);
        needsRendering = true;
    }

    public void clear() {
        subObjects = new HashMap<>();
        renderPriority = new TreeMap<>();
    }

    public void resize(Dimension d) {
        this.size = d;
        graphic = null;
        needsRendering = true;
    }

    public void changeView(Point p) {
        this.viewPoint.translate(p.x, p.y);
        needsRendering = true;
    }

    public Dimension getSize() {
        return size;
    }
}
