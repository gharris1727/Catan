package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Resizable graphic
 */
public abstract class TiledArea extends ObjectArea {

    //TODO: rework to allow for special edging.

    private Graphic texture;

    public TiledArea(Point position, int priority, Graphic texture) {
        super(position, priority);
        this.texture = texture;
    }

    public Point getObjectPosition(ScreenObject object) {
        return object.getPosition();
    }

    protected void render() {
        clear();
        int width = texture.getMask().getWidth();
        int height = texture.getMask().getHeight();
        for (int x = 0; x < getSize().width; x += width) {
            for (int y = 0; y < getSize().height; y += height) {
                add(new StaticObject(new Point(x, y), 0, texture) {
                    public Clickable getClickable(Point p) {
                        return TiledArea.this;
                    }

                    public String toString() {
                        return "TiledAreaTile " + getPosition();
                    }
                });
            }
        }
    }
}
