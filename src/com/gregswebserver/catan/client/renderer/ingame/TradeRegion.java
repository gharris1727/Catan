package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.screen.GridScreenRegion;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 * A trading screen that appears on the sidebar of the in-game screen.
 */
public class TradeRegion extends GridScreenRegion {

    public TradeRegion(Point position, int priority, Dimension size) {
        super(position, priority, size);
    }

    public void setSize(Dimension d) {
        //TODO: resize logic.
    }

    protected void render() {

    }

    public String toString() {
        return "TradeScreenArea";
    }
}
