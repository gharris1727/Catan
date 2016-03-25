package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.screen.Renderable;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;
import com.gregswebserver.catan.common.profiler.TimeSlice;

/**
 * Created by greg on 2/28/16.
 * Class representing a graphic object that depends on a UIStyle
 */
public abstract class StyledGraphicObject extends ScreenObject implements Graphical, Styled, Renderable {

    private UIStyle style;
    private boolean needsRender;

    protected StyledGraphicObject(int priority) {
        super(priority);
        style = null;
        needsRender = false;
    }

    @Override
    public boolean isGraphical() {
        return true;
    }

    @Override
    public boolean isAnimated() {
        return false;
    }

    @Override
    public boolean needsRender() {
        return needsRender;
    }

    @Override
    public void forceRender() {
        needsRender = true;
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }

    @Override
    public boolean isRenderable() {
        return style != null;
    }

    @Override
    public void assertRenderable() {
        if (style == null)
            throw new NotYetRenderableException(this + " has no style.");
    }

    @Override
    public TimeSlice getRenderTime() {
        return new TimeSlice("StyledGraphic");
    }
}
