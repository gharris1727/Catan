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

    protected int texWidth;
    protected int texHeight;
    protected int totWidth;
    protected int totHeight;

    public TiledBackground(int priority, GraphicSet style) {
        super(priority);
        this.style = style;
        //TODO: add texture tiling features using 16 tile tile sets
    }

    public GraphicSet getStyle() {
        return style;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        RenderMask textureMask = getStyle().getMask();
        texWidth = textureMask.getWidth();
        texHeight = textureMask.getHeight();
        totWidth = mask.getWidth();
        totHeight = mask.getHeight();
    }

    @Override
    protected void renderContents() {
        clear();
        for (int x = 0; x < totWidth; x += texWidth) {
            for (int y = 0; y < totHeight; y += texHeight) {
                addTile(new Point(x, y), Direction.center);
            }
        }
    }

    protected void addTile(Point position, Direction d) {
        add(new StaticObject(d.ordinal(), style.getGraphic(d.ordinal())) {
            public String toString() {
                return "Tile inside " + TiledBackground.this;
            }
        }).setClickable(this).setPosition(position);
    }
}
