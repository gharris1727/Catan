package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;

import static com.gregswebserver.catan.common.util.Direction.*;

/**
 * Created by Greg on 1/16/2015.
 * a TiledBackground that has additional edging shown.
 */
public abstract class EdgedTiledBackground extends TiledBackground {

    public EdgedTiledBackground(int priority, String backgroundStyle) {
        super(priority, backgroundStyle);
    }

    @Override
    protected void renderContents() {
        assertRenderable();
        graphics = getStyle().getBackgroundGraphics(backgroundStyle);
        RenderMask textureMask = graphics.getMask();
        int totWidth = getMask().getWidth();
        int totHeight = getMask().getHeight();
        int texHeight = textureMask.getHeight();
        int texWidth = textureMask.getWidth();
        int corWidth = totWidth - texWidth;
        int corHeight = totHeight - texHeight;
        //Four corners
        clear();
        addTile(new Point(), upleft);
        addTile(new Point(corWidth, 0), upright);
        addTile(new Point(0, corHeight), downleft);
        addTile(new Point(corWidth, corHeight), downright);
        //Top and bottom edges.
        for (int x = texWidth; x < corWidth; x += texWidth) {
            addTile(new Point(x, 0), up);
            addTile(new Point(x, corHeight), down);
        }
        //Left and right edges.
        for (int y = texHeight; y < corHeight; y += texHeight) {
            addTile(new Point(0, y), left);
            addTile(new Point(corWidth, y), right);
        }
        renderFillerTiles(0, 0, totWidth, totHeight);
    }

    private void addTile(Point position, Direction d) {
        add(new GraphicObject(d.ordinal(), graphics.getGraphic(d.ordinal())) {
            public String toString() {
                return "Tile inside " + EdgedTiledBackground.this;
            }
        }).setClickable(this).setPosition(position);
    }
}
