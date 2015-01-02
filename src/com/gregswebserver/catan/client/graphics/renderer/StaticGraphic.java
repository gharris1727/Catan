package com.gregswebserver.catan.client.graphics.renderer;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.input.clickables.Clickable;

import java.awt.*;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public class StaticGraphic extends ScreenObject {

    public StaticGraphic(Graphic graphic, Point position, int priority, Clickable clickable) {
        super(position, priority, clickable);
        this.graphic = graphic;
    }

    public boolean needsRendering() {
        return false;
    }

    public Graphic getGraphic() {
        return graphic;
    }

    public String toString() {
        return super.toString() + " StaticGraphic";
    }
}
