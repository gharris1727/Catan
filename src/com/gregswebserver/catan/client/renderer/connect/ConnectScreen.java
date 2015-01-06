package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ConnectScreen extends ColorScreenArea {

    public static final Dimension serverSize = new Dimension(256, 128);
    private ServerList list;
    private ServerListArea servers;
    private ServerEditDialogue detail;

    public ConnectScreen(ServerList list) {
        super(new Point(), 0);
        this.list = list;
        servers = new ServerListArea(new Point(), 0);
    }

}

