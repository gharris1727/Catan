package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.util.UniqueColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by Greg on 8/19/2014.
 * A screen object consisting of other screen objects with HitboxColors to differentiate them.
 */
public class ScreenArea implements ScreenObject {

    private HashMap<Integer, ScreenObject> subObjects;
    //TODO: evaluate TreeMap's effectiveness as a render priority manager.
    private TreeMap<Integer, ArrayList<ScreenObject>> renderPriority;
    private Graphic graphic;
    private boolean needsRendering;
    private Point position;
    private Point viewPoint;
    private int color;
    private int priority;

    public ScreenArea(Dimension size, Point position, int priority) {
        clear();
        this.position = position;
        this.viewPoint = new Point();
        this.color = UniqueColor.getNext();
        this.priority = priority;
        this.needsRendering = false;
        this.graphic = new Graphic(size);
    }

    public Object getClickable(Point p) {
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        int color = getGraphic().getHitboxColor(subPosition);
        ScreenObject object = subObjects.get(color);
        if (object instanceof ScreenArea)
            return object.getClickable(p);
        return object;
    }

    public int getRenderPriority() {
        return priority;
    }

    public boolean needsRendering() {
        if (needsRendering) return true;
        for (ScreenObject object : subObjects.values()) {
            if (object.needsRendering()) return true;
        }
        return false;
    }

    public Point getPosition() {
        return position;
    }

    public int getHitboxColor() {
        return color;
    }

    public Graphic getGraphic() {
        if (needsRendering()) {
            render();
        }
        return graphic;
    }

    private void render() {
        graphic.clear();
        for (ScreenObject object : subObjects.values()) {
            Point position = object.getPosition();
            Point renderPosition = new Point(position.x - viewPoint.x, position.y - viewPoint.y);
            object.getGraphic().renderTo(graphic, null, renderPosition, object.getHitboxColor());
        }
        needsRendering = false;
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
        graphic = new Graphic(d);
        needsRendering = true;
    }

    public void changeView(Point p) {
        this.viewPoint.translate(p.x, p.y);
        needsRendering = true;
    }
}
