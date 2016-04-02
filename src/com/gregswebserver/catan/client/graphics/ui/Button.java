package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.input.UserEvent;

import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends DefaultConfigurableScreenRegion {

    private final TiledBackground background;
    private final TextLabel text;

    protected Button(int priority, String configKey, String label) {
        super(priority, configKey);
        background = new EdgedTiledBackground(0, "background");
        text = new TextLabel(1, "label", label);
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    public void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public abstract UserEvent onMouseClick(MouseEvent event);

    @Override
    protected void renderContents() {
        center(text);
    }
}
