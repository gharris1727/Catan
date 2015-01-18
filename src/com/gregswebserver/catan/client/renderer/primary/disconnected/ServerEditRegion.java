package com.gregswebserver.catan.client.renderer.primary.disconnected;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;

import java.awt.Point;

/**
 * Created by Greg on 1/5/2015.
 * A dialogue that pops up on the screen when a user wants to edit/create a new ServerList entry.
 */
public class ServerEditRegion extends UIScreenRegion {

    ConnectionInfo info;

    public ServerEditRegion(Point position, int priority, RenderMask mask, UIStyle style, ConnectionInfo info) {
        super(position, priority, mask, style);
        this.info = info;
    }

    public String toString() {
        return "ServerEditDialogue " + info;
    }
}
