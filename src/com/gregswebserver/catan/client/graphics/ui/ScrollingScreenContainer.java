package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.*;

/**
 * Created by greg on 2/7/16.
 * An integrated container for a ScrollingScreenRegion.
 */
public class ScrollingScreenContainer extends ConfigurableScreenRegion implements Updatable {

    private final ScrollingScreenRegion scroll;

    public ScrollingScreenContainer(String name, int priority, ScrollingScreenRegion scroll) {
        super(name, priority, "ignored");
        enableTransparency();
        this.scroll = scroll;
        scroll.setHost(this);
        add(scroll);
    }

    @Override
    public void setConfig(UIConfig config) {
        //This is specifically designed to counteract the .narrow call and prevent the narrowing.
        super.setConfig(new UIConfig(config) {
            @Override
            public UIConfig narrow(String prefix) {
                return config;
            }
        });
    }

    @Override
    public void update() {
        scroll.update();
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
