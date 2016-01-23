package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.resources.GraphicSet;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Resizable graphic
 */
public abstract class TiledBackground extends UIScreenRegion {

    private final String backgroundStyle;

    private GraphicSet graphics;
    protected int texWidth;
    protected int texHeight;
    protected int totWidth;
    protected int totHeight;

    public TiledBackground(int priority, String backgroundStyle) {
        super(priority);
        this.backgroundStyle = backgroundStyle;
        //TODO: add texture tiling features using 16 tile tile sets
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        totWidth = mask.getWidth();
        totHeight = mask.getHeight();
    }

    @Override
    protected void renderContents() {
        clear();
        graphics = getStyle().getBackgroundStyle(backgroundStyle).getGraphicSet();
        RenderMask textureMask = graphics.getMask();
        texWidth = textureMask.getWidth();
        texHeight = textureMask.getHeight();
        for (int x = 0; x < totWidth; x += texWidth) {
            for (int y = 0; y < totHeight; y += texHeight) {
                addTile(new Point(x, y), Direction.center);
            }
        }
    }

    protected void addTile(Point position, Direction d) {
        add(new GraphicObject(d.ordinal(), graphics.getGraphic(d.ordinal())) {
            public String toString() {
                return "Tile inside " + TiledBackground.this;
            }
        }).setClickable(this).setPosition(position);
    }
}
