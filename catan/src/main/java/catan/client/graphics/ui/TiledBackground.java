package catan.client.graphics.ui;

import catan.client.graphics.graphics.Graphic;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.common.resources.GraphicSet;
import catan.common.util.Direction;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Resizable graphic
 */
public class TiledBackground extends DefaultConfigurableScreenRegion {

    protected GraphicSet graphics;

    public TiledBackground() {
        this("Background", 0, "background");
        //TODO: add texture tiling features using 16 tile tile sets
    }

    public TiledBackground(String name, int priority, String configKey) {
        super(name, priority, configKey);
    }

    @Override
    public void loadConfig(UIConfig config) {
        super.loadConfig(config);
        graphics = config.getBackgroundGraphics(config.getLayout().get("style"));
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        clear();
        renderFillerTiles(getMask().getWidth(),getMask().getHeight());
    }

    protected void renderFillerTiles(int xStop, int yStop) {
        RenderMask textureMask = graphics.getMask();
        int texWidth = textureMask.getWidth();
        int texHeight = textureMask.getHeight();
        Graphic center = graphics.getGraphic(Direction.center.ordinal());
        for (int x = 0; x < xStop; x += texWidth) {
            for (int y = 0; y < yStop; y += texHeight) {
                GraphicObject tile = new GraphicObject("Tile inside " + this, 0, center);
                add(tile).setClickable(this).setPosition(new Point(x,y));
            }
        }
    }
}
