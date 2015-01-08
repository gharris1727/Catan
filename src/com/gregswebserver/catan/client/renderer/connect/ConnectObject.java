package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.graphics.screen.ColorObjectArea;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ConnectObject extends ColorObjectArea {

    private ServerList list;
    private ServerListArea servers;
    private ServerEditDialogue detail;

    public ConnectObject(ServerList list) {
        super(new Point(), 0);
        this.list = list;
        servers = new ServerListArea(new Point(), 0, list);
        add(servers);
    }

    public void setSize(Dimension d) {
        servers.setSize(d);
        super.setSize(d);
    }

    public String toString() {
        return "ConnectScreen";
    }

}

