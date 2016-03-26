package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.common.resources.GraphicSet;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Resizable graphic
 */
public class TiledBackground extends ConfigurableScreenRegion {

    protected GraphicSet graphics;

    public TiledBackground(int priority, String configKey) {
        super(priority, configKey);
        //TODO: add texture tiling features using 16 tile tile sets
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        graphics = getConfig().getBackgroundGraphics(getConfig().getLayout().get("style"));
        clear();
        renderFillerTiles(0,0,getMask().getWidth(),getMask().getHeight());
    }

    protected void renderFillerTiles(int xStart, int yStart, int xStop, int yStop) {
        RenderMask textureMask = graphics.getMask();
        int texWidth = textureMask.getWidth();
        int texHeight = textureMask.getHeight();
        Graphic center = graphics.getGraphic(Direction.center.ordinal());
        for (int x = xStart; x < xStop; x += texWidth) {
            for (int y = yStart; y < yStop; y += texHeight) {
                add(new GraphicObject(0, center) {
                    public String toString() {
                        return "Tile inside " + TiledBackground.this;
                    }
                }).setClickable(this).setPosition(new Point(x,y));
            }
        }
    }

    public String toString() {
        return "TiledBackground";
    }
}
