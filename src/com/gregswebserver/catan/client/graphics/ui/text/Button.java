package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public abstract class Button extends UIScreenRegion {

    private final TiledBackground background;
    private final TextLabel text;

    public Button(int priority, String label) {
        super(priority);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            public String toString() {
                return "ButtonBackground";
            }
        };
        text = new TextLabel(1, UIStyle.FONT_HEADING, label) {
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
    }

    @Override
    protected void restyleContents(UIStyle style) {
        //Restyle the background
        background.setStyle(style);
        //Restyle the text label
        text.setStyle(style);
    }

    @Override
    protected void renderContents() {
        text.getPosition().setLocation(getCenteredPosition(text.getGraphic().getMask()));
    }
}
