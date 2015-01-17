package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends UIScreenRegion {

    private boolean toggle;
    private boolean state;

    public Button(Point position, int priority, RenderMask mask, UIStyle style, boolean toggle) {
        super(position, priority, mask, style);
        this.toggle = toggle;
    }

    public boolean getState() {
        return state;
    }


}
