package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.input.Clickable;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Resizable graphic
 */
public abstract class SimpleTiledBackground extends ScreenRegion {

    private Graphic texture;

    public SimpleTiledBackground(Point position, int priority, Dimension size, Graphic texture) {
        super(position, priority);
        setSize(size);
        this.texture = texture;
    }

    protected Point getObjectPosition(ScreenObject object) {
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
                        return SimpleTiledBackground.this;
                    }

                    public String toString() {
                        return "TiledAreaTile " + getPosition();
                    }
                });
            }
        }
    }
}
