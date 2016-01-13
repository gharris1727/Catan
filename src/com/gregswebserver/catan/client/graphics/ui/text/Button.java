package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends UIScreenRegion {

    private ScreenRegion background;
    private StaticObject text;

    public Button(int priority, UIStyle style, String label) {
        super(priority, style);
        background = new EdgedTiledBackground(0, getStyle().getButtonStyle()) {
            public String toString() {
                return "ButtonBackground";
            }
        };
        text = new TextLabel(1, getStyle().getDarkTextStyle(), label) {
            public String toString() {
                return "ButtonLabel";
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    public void resizeContents(RenderMask mask) {
        background.setMask(mask);
        text.getPosition().setLocation(getCenteredPosition(text.getGraphic().getMask()));
    }
}
