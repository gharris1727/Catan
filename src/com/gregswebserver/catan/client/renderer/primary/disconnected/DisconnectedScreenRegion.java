package com.gregswebserver.catan.client.renderer.primary.disconnected;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.Point;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class DisconnectedScreenRegion extends UIScreenRegion {

    private ScreenRegion background;
    private ServerListRegion servers;
    private ServerEditRegion detail;

    public DisconnectedScreenRegion(RenderMask mask, UIStyle style, ServerList list) {
        super(new Point(), 0, mask, style);
        background = new EdgedTiledBackground(new Point(), 0, mask, style.getBackgroundStyle()) {
            public String toString() {
                return "DisconnectedScreenBackground";
            }
        };
        add(background).setClickable(this);
        servers = new ServerListRegion(new Point(), 1, mask, style, list);
        add(servers);
    }

    public void resizeContents(RenderMask mask) {
        background.resize(mask);
        servers.resize(mask);
    }

    public String toString() {
        return "DisconnectedScreen";
    }

}

