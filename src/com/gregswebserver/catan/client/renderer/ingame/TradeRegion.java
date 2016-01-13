package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends ScreenRegion {

    public TradeRegion(int priority) {
        super(priority);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    public String toString() {
        return "TradeScreenArea";
    }
}
