package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.input.UserEvent;

import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends StyledScreenRegion {

    private final TiledBackground background;
    private final TextLabel text;

    public Button(int priority, String label) {
        super(priority);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_BUTTON);
        text = new TextLabel(1, UIStyle.FONT_HEADING, label);
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
