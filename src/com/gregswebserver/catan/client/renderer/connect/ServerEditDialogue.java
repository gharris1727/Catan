package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.common.crypto.ServerLogin;

import java.awt.*;

/**
 * Created by Greg on 1/5/2015.
 */
public class ServerEditDialogue extends ColorScreenArea {

    public ServerEditDialogue(Point position, int priority, ServerLogin login) {
        super(position, priority);
    }
}
