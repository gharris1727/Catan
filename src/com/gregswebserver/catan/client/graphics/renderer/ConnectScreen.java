package com.gregswebserver.catan.client.graphics.renderer;

import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.client.graphics.areas.GridScreenArea;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.crypto.ServerLogin;

import java.awt.*;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ConnectScreen extends ColorScreenArea {

    private ServerList list;

    private ServerListArea servers;
    private ServerDetailDialogue detail;

    public ConnectScreen(ServerList list) {
        super(new Point(), 0);
        this.list = list;
        clickable = new ConnectScreenClickable();
    }

    private class ConnectScreenClickable implements Clickable {
    }

    private class ServerListArea extends GridScreenArea {

        public ServerListArea(Point position, int priority) {
            super(position, priority);
            clickable = new ServerListClickable();
        }

        @Override
        public void resize(Dimension d) {

        }
    }

    private class ServerListClickable implements Clickable {

    }

    private class ServerDetailDialogue extends ColorScreenArea {

        public ServerDetailDialogue(Point position, int priority, ServerLogin login) {
            super(position, priority);
            clickable = new ServerScreenClickable();
        }

    }

    private class ServerScreenClickable implements Clickable {
    }
}
