package com.gregswebserver.catan.client.graphics.ui.style;

import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
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
        for (ScreenObject o : this) {
            if (o instanceof Styled)
                ((Styled) o).setStyle(style);
        }
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
}
