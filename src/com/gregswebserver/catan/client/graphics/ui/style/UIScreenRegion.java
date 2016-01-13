package com.gregswebserver.catan.client.graphics.ui.style;

import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;

/**
 * Created by Greg on 1/16/2015.
 * A screen region used as a base for the user interface.
 */
public abstract class UIScreenRegion extends ScreenRegion {

    private UIStyle style;

    public UIScreenRegion(int priority, UIStyle style) {
        super(priority);
        this.style = style;
    }

    public UIStyle getStyle() {
        return style;
    }
}
