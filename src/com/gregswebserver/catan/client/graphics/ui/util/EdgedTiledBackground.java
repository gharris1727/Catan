package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.resources.GraphicSet;

import java.awt.*;

import static com.gregswebserver.catan.common.util.Direction.*;

/**
 * Created by Greg on 1/16/2015.
 * a TiledBackground that has additional edging shown.
 */
public abstract class EdgedTiledBackground extends TiledBackground {

    public EdgedTiledBackground(Point position, int priority, RenderMask mask, GraphicSet textures) {
        super(position, priority, mask, textures);
    }

    protected void renderContents() {
        super.renderContents();
        RenderMask mask = getStyle().getMask();
        int texWidth = mask.getWidth();
        int texHeight = mask.getHeight();
        int totWidth = getMask().getWidth();
        int totHeight = getMask().getHeight();
        int corWidth = totWidth - texWidth;
        int corHeight = totHeight - texHeight;
        //Four corners
        addTile(new Point(), upleft);
        addTile(new Point(corWidth, 0), upright);
        addTile(new Point(0, corHeight), downleft);
        addTile(new Point(corWidth, corHeight), downright);
        //Top and bottom edges.
        for (int x = 0; x < totWidth; x += texWidth) {
            addTile(new Point(x, 0), up);
            addTile(new Point(x, corHeight), down);
        }
        //Left and right edges.
        for (int y = 0; y < totHeight; y += texHeight) {
            addTile(new Point(0, y), left);
            addTile(new Point(corWidth, y), right);
        }
    }

}
