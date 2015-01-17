package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.util.Graphic;
import com.gregswebserver.catan.client.graphics.util.TextGraphic;

import java.awt.*;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends UIScreenRegion {

    private String label;
    private ScreenRegion background;

    public Button(Point position, int priority, RenderMask mask, UIStyle style, String label) {
        super(position, priority, mask, style);
        this.label = label;
        background = new EdgedTiledBackground(new Point(), 0, getMask(), getStyle().getButtonStyle()) {
            public String toString() {
                return "ButtonBackground";
            }
        };
        background.setClickable(this);
    }

    public void setMask(RenderMask mask) {
        if (background != null)
            background.setMask(mask);
        super.setMask(mask);
    }

    protected void render() {
        clear();
        add(background);

        Graphic textGraphic = new TextGraphic(getStyle().getDarkTextStyle(), label);
        Point position = getCenteredPosition(textGraphic.getMask());
        add(new StaticObject(position, 1, textGraphic) {
            public String toString() {
                return "ButtonLabel";
            }
        }).setClickable(this);
    }

}
