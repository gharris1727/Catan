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

    public ConnectingScreenRegion(UIStyle style) {
        super(0, style);
        background = new EdgedTiledBackground(0,style.getBackgroundStyle()) {
            public String toString() {
                return "DisconnectingScreen Background";
            }
        };
        textGraphic = new TextGraphic(style.getDarkTextStyle(), "Connecting...");
        text = new StaticObject(1, textGraphic) {
            public String toString() {
                return "Disconnect Reason";
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        text.getPosition().setLocation(getCenteredPosition(textGraphic.getMask()));
    }

    public String toString() {
        return "ConnectingScreen";
    }
}
