package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.areas.ColorScreenArea;
import com.gregswebserver.catan.client.graphics.areas.GridScreenArea;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/5/2015.
 */
public class ServerListArea extends GridScreenArea {

    public ServerListArea(Point position, int priority) {
        super(position, priority);
    }

    public void resize(Dimension d) {

    }

    private class ServerListItemArea extends ColorScreenArea {

        private final int listIndex;

        public ServerListItemArea(Point position, int priority, int listIndex) {
            super(position, priority);
            this.listIndex = listIndex;
            resize(ConnectScreen.serverSize);
        }

        public UserEvent onMouseClick(MouseEvent event) {
            return new UserEvent(this, UserEventType.Server_Clicked, listIndex);
        }
    }
}
