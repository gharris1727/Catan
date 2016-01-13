package com.gregswebserver.catan.client.renderer.primary.disconnected;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.ui.primary.ServerPool;

import java.awt.Point;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class DisconnectedScreenRegion extends UIScreenRegion {

    private ScreenRegion background;
    private ServerListRegion servers;
    private ServerEditRegion detail;

    public DisconnectedScreenRegion(UIStyle style, ServerPool list) {
        super(0, style);
        background = new EdgedTiledBackground(0,  style.getBackgroundStyle()) {
            public String toString() {
                return "DisconnectedScreenBackground";
            }
        };
        add(background).setClickable(this);
        servers = new ServerListRegion(1, style, list);
        add(servers);
    }

    public void resizeContents(RenderMask mask) {
        background.setMask(mask);
        servers.setMask(mask);
    }

    public String toString() {
        return "DisconnectedScreen";
    }

}

