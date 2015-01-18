package com.gregswebserver.catan.client.renderer.primary.connecting;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;

import java.awt.Point;

/**
 * Created by Greg on 1/18/2015.
 * A screen that is shown while the client is connecting to a remote server.
 */
public class ConnectingScreenRegion extends UIScreenRegion {

    private ScreenRegion background;
    private Graphic textGraphic;
    private ScreenObject text;

    public ConnectingScreenRegion(RenderMask mask, UIStyle style) {
        super(new Point(), 0, mask, style);
        background = new EdgedTiledBackground(new Point(), 0, mask, style.getBackgroundStyle()) {
            public String toString() {
                return "DisconnectingScreen Background";
            }
        };
        textGraphic = new TextGraphic(style.getDarkTextStyle(), "Connecting...");
        text = new StaticObject(new Point(), 1, textGraphic) {
            public String toString() {
                return "Disconnect Reason";
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
        resizeContents(mask);
    }

    protected void resizeContents(RenderMask mask) {
        background.resize(mask);
        Point textPosition = text.getPosition();
        Point textCenter = getCenteredPosition(textGraphic.getMask());
        textPosition.x = textCenter.x;
        textPosition.y = textCenter.y;
    }

    public String toString() {
        return "ConnectingScreen";
    }
}
