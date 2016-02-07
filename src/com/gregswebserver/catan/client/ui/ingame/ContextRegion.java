package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;

/**
 * Created by Greg on 1/6/2015.
 * A screen region that lives in the bottom corner of the in-game screen.
 */
public class ContextRegion extends UIScreenRegion {

    private final TiledBackground background;

    public ContextRegion(int priority) {
        super(priority);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_GAME) {
            @Override
            public String toString() {
                return "ContextRegionBackground";
            }
        };
        add(background);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    public String toString() {
        return "ContextScreenArea";
    }
}
