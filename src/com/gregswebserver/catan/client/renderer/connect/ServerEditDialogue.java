package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.screen.ColorObjectArea;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 * A dialogue that pops up on the screen when a user wants to edit/create a new ServerList entry.
 */
public class ServerEditDialogue extends ColorObjectArea {

    ConnectionInfo info;

    public ServerEditDialogue(Point position, int priority, ConnectionInfo info) {
        super(position, priority);
        this.info = info;
    }

    public String toString() {
        return "ServerEditDialogue " + info;
    }
}
