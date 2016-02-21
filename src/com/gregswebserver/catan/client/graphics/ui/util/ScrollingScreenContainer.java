package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;

import java.awt.*;

/**
 * Created by greg on 2/7/16.
 * An integrated container for a ScrollingScreenRegion.
 */
public abstract class ScrollingScreenContainer extends UIScreenRegion {

    private final ScrollingScreenRegion scroll;
    private final Insets insets;

    protected ScrollingScreenContainer(int priority, ScrollingScreenRegion scroll, Insets insets) {
        super(priority);
        this.scroll = scroll;
        this.insets = insets;
        add(scroll);
    }

    public void update() {
        scroll.forceRender();
    }

    public void center() {
        scroll.center();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        scroll.setHostView(mask, insets);
    }
}
