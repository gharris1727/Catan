package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.input.UserEventListener;

import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends DefaultConfigurableScreenRegion {

    private final TiledBackground background;
    private final TextLabel text;

    protected Button(String name, int priority, String configKey, String label) {
        super(name, priority, configKey);
        background = new EdgedTiledBackground();
        text = new TextLabel(name + "Label", 1, "label", label);
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    public void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public abstract void onMouseClick(UserEventListener listener, MouseEvent event);

    @Override
    protected void renderContents() {
        center(text);
    }
}
