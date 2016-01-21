package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;

/**
 * Created by Greg on 1/6/2015.
 * A screen region that lives in the bottom corner of the in-game screen.
 */
public class ContextRegion extends ScreenRegion {

    public ContextRegion(int priority) {
        super(priority);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    public String toString() {
        return "ContextScreenArea";
    }
}
