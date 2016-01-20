package com.gregswebserver.catan.client.graphics.ui.style;

import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;

/**
 * Created by Greg on 1/16/2015.
 * A screen region used as a base for the user interface.
 */
public abstract class UIScreenRegion extends ScreenRegion implements Styled {

    private UIStyle style;

    public UIScreenRegion(int priority) {
        super(priority);
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
        restyleContents(style);
        forceRender();
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }

    @Override
    public boolean isRenderable() {
        return super.isRenderable() && style != null;
    }

    protected abstract void restyleContents(UIStyle style);
}
