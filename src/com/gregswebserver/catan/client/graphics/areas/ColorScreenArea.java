package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 8/19/2014.
 * A screen object consisting of other screen objects with HitboxColors to differentiate them.
 */
public abstract class ColorScreenArea extends ScreenArea {

    private Map<Integer, ScreenObject> hitboxMap;

    public ColorScreenArea(Point position, int priority) {
        super(position, priority);
    }

    public Clickable getClickable(Point p) {
        int color = getGraphic().getHitboxColor(p);
        ScreenObject object = hitboxMap.get(color);
        Point position = getObjectPosition(object);
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        return (object == null) ? clickable : object.getClickable(subPosition);
    }

    public void add(ScreenObject object) {
        if (object == null) return;
        hitboxMap.put(object.getHitboxColor(), object);
        super.add(object);
    }

    public void clear() {
        hitboxMap = new HashMap<>();
        super.clear();
    }

    public Point getObjectPosition(ScreenObject object) {
        return object.getPosition();
    }
}
