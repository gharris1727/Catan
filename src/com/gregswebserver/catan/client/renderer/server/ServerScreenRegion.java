package com.gregswebserver.catan.client.renderer.server;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.common.lobby.ClientPool;

import java.awt.*;

/**
 * Created by Greg on 1/17/2015.
 * The list of lobbies hosted on the current server.
 */
public class ServerScreenRegion extends UIScreenRegion {

    public ServerScreenRegion(RenderMask screenMask, UIStyle style, ClientPool payload) {
        super(new Point(), 0, screenMask, style);
    }

    public String toString() {
        return "ServerScreenRegion";
    }
}
