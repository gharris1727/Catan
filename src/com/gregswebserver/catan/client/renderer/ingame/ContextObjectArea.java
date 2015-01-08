package com.gregswebserver.catan.client.renderer.ingame;

import com.gregswebserver.catan.client.graphics.screen.GridObjectArea;

import java.awt.*;

/**
 * Created by Greg on 1/6/2015.
 */
class ContextObjectArea extends GridObjectArea {

    public ContextObjectArea(Point position, int priority) {
        super(position, priority);
    }

    public void setSize(Dimension d) {

    }

    protected void render() {

    }

    public String toString() {
        return "ContextScreenArea";
    }
}
