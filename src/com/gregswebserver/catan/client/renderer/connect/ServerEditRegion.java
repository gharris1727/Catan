package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.screen.ColorScreenRegion;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 * A dialogue that pops up on the screen when a user wants to edit/create a new ServerList entry.
 */
public class ServerEditRegion extends ColorScreenRegion {

    ConnectionInfo info;

    public ServerEditRegion(Point position, int priority, Dimension size, ConnectionInfo info) {
        super(position, priority, size);
        this.info = info;
    }

    public String toString() {
        return "ServerEditDialogue " + info;
    }
}
