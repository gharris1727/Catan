package com.gregswebserver.catan.client.graphics;

import com.gregswebserver.catan.client.input.Clickable;
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
    private final Clickable clickable;

    public StaticGraphic(Graphic graphic, Point position, int priority, Clickable clickable) {
        this.graphic = graphic;
        this.position = position;
        this.color = UniqueColor.getNext();
        this.priority = priority;
        this.clickable = clickable;
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

    public Clickable getClickable(Point p) {
        return clickable;
    }

    public int getRenderPriority() {
        return 0;
    }

    public boolean needsRendering() {
        return false; //Never needs re-rendering.
    }

    public String toString() {
        return "StaticGraphic Position: " + position + " Color: " + color + " Priority: " + priority + " Clickable: " + clickable;
    }
}
