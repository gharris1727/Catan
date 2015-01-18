package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends ScreenRegion {

    public TradeRegion(Point position, int priority, RenderMask mask) {
        super(position, priority, mask);
    }

    public String toString() {
        return "TradeScreenArea";
    }
}
