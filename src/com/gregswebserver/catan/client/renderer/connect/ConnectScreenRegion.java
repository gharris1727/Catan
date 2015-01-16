package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.screen.ColorScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.SimpleTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.UIBackground;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.util.Direction;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ConnectScreenRegion extends ColorScreenRegion {

    private ScreenRegion background;
    private ServerListRegion servers;
    private ServerEditRegion detail;

    public ConnectScreenRegion(Dimension size, ServerList list) {
        super(new Point(), 0, size);
        background = new SimpleTiledBackground(new Point(), 0, size, UIBackground.Blue.getGraphic(Direction.center)) {
            public String toString() {
                return "ConnectScreenBackground";
            }
        };
        add(background);
        servers = new ServerListRegion(new Point(), 0, size, list);
        add(servers);
    }

    public void setSize(Dimension d) {
        if (background != null)
            background.setSize(d);
        if (servers != null)
            servers.setSize(d);
        super.setSize(d);
    }

    public String toString() {
        return "ConnectScreen";
    }

}

