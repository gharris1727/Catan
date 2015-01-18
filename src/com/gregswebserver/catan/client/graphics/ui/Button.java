package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends UIScreenRegion {

    private String label;
    private ScreenRegion background;
    private StaticObject text;

    public Button(Point position, int priority, RenderMask mask, UIStyle style, String label) {
        super(position, priority, mask, style);
        this.label = label;
        background = new EdgedTiledBackground(new Point(), 0, getMask(), getStyle().getButtonStyle()) {
            public String toString() {
                return "ButtonBackground";
            }
        };
        Graphic textGraphic = new TextGraphic(getStyle().getDarkTextStyle(), label);
        text = new StaticObject(getCenteredPosition(textGraphic.getMask()), 1, textGraphic) {
            public String toString() {
                return "ButtonLabel";
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    public void resizeContents(RenderMask mask) {
        background.resize(mask);
    }
}
