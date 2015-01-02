package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.renderer.ScreenObject;
import com.gregswebserver.catan.client.input.clickables.Clickable;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Greg on 8/19/2014.
 * A screen object consisting of other screen objects with HitboxColors to differentiate them.
 */
public abstract class ColorScreenArea extends ScreenArea {

    private Map<Integer, ScreenObject> hitboxMap;

    public ColorScreenArea(Point position, int priority, Clickable clickable) {
        super(position, priority, clickable);
    }

    public Clickable getClickable(Point p) {
        int color = getGraphic().getHitboxColor(p);
        ScreenObject object = hitboxMap.get(color);
        //TODO: check this line for accuracy.
        Point subPosition = new Point(p.x - position.x, p.y - position.y);
        return (object == null) ? clickable : object.getClickable(subPosition);
    }

    public void add(ScreenObject object) {
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
