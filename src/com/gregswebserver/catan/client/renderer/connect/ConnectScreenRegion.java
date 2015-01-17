package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ColorScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.UIStyle;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ConnectScreenRegion extends ColorScreenRegion {

    private ScreenRegion background;
    private ServerListRegion servers;
    private ServerEditRegion detail;

    public ConnectScreenRegion(RenderMask mask, UIStyle style, ServerList list) {
        super(new Point(), 0, mask);
        background = new EdgedTiledBackground(new Point(), 0, mask, style.getBackgroundStyle()) {
            public String toString() {
                return "ConnectScreenBackground";
            }
        };
        add(background);
        servers = new ServerListRegion(new Point(), 1, mask, style, list);
        add(servers);
    }

    public void setMask(RenderMask mask) {
        if (background != null)
            background.setMask(mask);
        if (servers != null)
            servers.setMask(mask);
        super.setMask(mask);
    }

    public String toString() {
        return "ConnectScreen";
    }

}

