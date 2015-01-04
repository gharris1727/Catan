package com.gregswebserver.catan.client.graphics.areas;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public class StaticGraphic extends ScreenObject {

    public StaticGraphic(Point position, int priority, Graphic graphic, Clickable clickable) {
        super(position, priority);
        this.graphic = graphic;
        this.clickable = clickable;
    }

    public boolean needsRender() {
        return false;
    }

    public boolean canRender() {
        return true;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public String toString() {
        return "StaticGraphic";
    }
}
