package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Resizable graphic
 */
public abstract class TiledBackground extends ScreenRegion {

    private GraphicSet style;

    public TiledBackground(Point position, int priority, RenderMask mask, GraphicSet style) {
        super(position, priority, mask);
        this.style = style;
    }

    public GraphicSet getStyle() {
        return style;
    }

    protected void renderContents() {
        clear();
        RenderMask mask = getStyle().getMask();
        int texWidth = mask.getWidth();
        int texHeight = mask.getHeight();
        int totWidth = getMask().getWidth();
        int totHeight = getMask().getHeight();

        for (int x = 0; x < totWidth; x += texWidth) {
            for (int y = 0; y < totHeight; y += texHeight) {
                addTile(new Point(x, y), Direction.center);
            }
        }
    }

    protected void addTile(Point position, Direction d) {
        add(new StaticObject(position, d.ordinal(), style.getGraphic(d.ordinal())) {
            public String toString() {
                return "Tile inside " + TiledBackground.this;
            }
        }).setClickable(this);
    }
}
