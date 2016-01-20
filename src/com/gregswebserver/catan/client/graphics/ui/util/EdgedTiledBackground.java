package com.gregswebserver.catan.client.graphics.ui.util;

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
        super.renderContents();
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
