package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * A screen region used as a base for the user interface.
 */
public abstract class UIScreenRegion extends ScreenRegion {

    private UIStyle style;

    public UIScreenRegion(Point position, int priority, RenderMask mask, UIStyle style) {
        super(position, priority, mask);
        this.style = style;
    }

    public UIStyle getStyle() {
        return style;
    }
}
