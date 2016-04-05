package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;

/**
 * Created by greg on 2/7/16.
 * An integrated container for a ScrollingScreenRegion.
 */
public abstract class ScrollingScreenContainer extends ConfigurableScreenRegion {

    private final ScrollingScreenRegion scroll;

    protected ScrollingScreenContainer(int priority, String configKey, ScrollingScreenRegion scroll) {
        super(priority, configKey);
        this.scroll = scroll;
        scroll.setHost(this);
        add(scroll);
    }

    public void update() {
        scroll.forceRender();
    }

    public void center() {
        scroll.center();
    }

    public void setInsets(Insets insets) {
        scroll.setInsets(insets);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        scroll.resizeContents(mask);
    }

    public ScrollingScreenRegion getScroll() {
        return scroll;
    }
}
