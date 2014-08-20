package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.util.UniqueColor;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public class StaticGraphic implements ScreenObject {

    private final Graphic graphic;
    private final Point position;
    private final int color;
    private final int priority;
    private final Object object;

    public StaticGraphic(Graphic graphic, Point position, int priority, Object object) {
        this.graphic = graphic;
        this.position = position;
        this.color = UniqueColor.getNext();
        this.priority = priority;
        this.object = object;
    }

    public Point getPosition() {
        return position;
    }

    public int getHitboxColor() {
        return color;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public Object getObject(Point p) {
        return null;
    }

    public int getRenderPriority() {
        return 0;
    }

    public boolean needsRendering() {
        return false; //Never needs re-rendering.
    }
}
