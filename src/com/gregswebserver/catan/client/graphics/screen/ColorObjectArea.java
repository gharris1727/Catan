package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 8/19/2014.
 * A screen object consisting of other screen objects with HitboxColors to differentiate them.
 */
public abstract class ColorObjectArea extends ObjectArea {

    private Map<Integer, ScreenObject> hitboxMap;

    public ColorObjectArea(Point position, int priority) {
        super(position, priority);
    }

    public Clickable getClickable(Point p) {
        int color = getGraphic().getHitboxColor(p);
        ScreenObject object = hitboxMap.get(color);
        if (object == null)
            return this;
        Point position = getObjectPosition(object);
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        return object.getClickable(subPosition);
    }

    public ScreenObject add(ScreenObject object) {
        if (object == null) return null;
        hitboxMap.put(object.getHitboxColor(), object);
        return super.add(object);
    }

    public ScreenObject remove(ScreenObject object) {
        if (object == null) return null;
        hitboxMap.remove(object.getHitboxColor());
        return super.remove(object);
    }

    public void clear() {
        hitboxMap = new HashMap<>();
        super.clear();
    }

    public Point getObjectPosition(ScreenObject object) {
        return object.getPosition();
    }
}
