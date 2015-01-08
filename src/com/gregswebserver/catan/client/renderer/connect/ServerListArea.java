package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.screen.ColorObjectArea;
import com.gregswebserver.catan.client.graphics.screen.GridObjectArea;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListArea extends GridObjectArea {

    private static final Dimension serverSize = new Dimension(256, 128);
    private static final Dimension buttonPanelSize = new Dimension(serverSize.width, 256);
    private static final int vertPadding = 128;
    private final ServerList list;
    private int scroll;
    private int displayed;
    private int selected;

    public ServerListArea(Point position, int priority, ServerList list) {
        super(position, priority);
        this.list = list;
        scroll = 0;
        displayed = 0;
        selected = -1;
    }

    public void setSize(Dimension d) {
        int wPadding = d.width - serverSize.width;
        int lPadding = wPadding / 2;
        int rPadding = wPadding / 2 + wPadding % 2;
        int[] widths = new int[]{lPadding, serverSize.width, rPadding};
        displayed = ((d.height - vertPadding - buttonPanelSize.height) / serverSize.height) - 1;
        int[] heights = new int[displayed + 3];
        int vPadding = d.height - serverSize.height * displayed - buttonPanelSize.height;
        int uPadding = vPadding / 2;
        int dPadding = vPadding / 2 + vPadding % 2;
        heights[0] = uPadding;
        for (int i = 0; i < displayed; i++)
            heights[i + 1] = serverSize.height;
        heights[heights.length - 2] = buttonPanelSize.height;
        heights[heights.length - 1] = dPadding;
        resize(widths, heights);
    }

    protected void render() {
        clear();
        for (int i = 0; i < displayed; i++)
            add(new ServerListItemArea(new Point(1, i + 1), 0, i + scroll));
        add(new ButtonPanelArea(new Point(1, displayed + 1), 0));
    }

    public String toString() {
        return "ServerListArea";
    }

    private class ServerListItemArea extends ColorObjectArea {

        private final int listIndex;
        private final ConnectionInfo info;

        public ServerListItemArea(Point position, int priority, int listIndex) {
            super(position, priority);
            this.listIndex = listIndex;
            info = list.get(listIndex);
            setSize(serverSize);
        }

        public UserEvent onMouseClick(MouseEvent event) {
            selected = listIndex;
            return null;
        }

        public String toString() {
            return "ServerListItemArea " + listIndex;
        }
    }

    private class ButtonPanelArea extends ColorObjectArea {

        public ButtonPanelArea(Point position, int priority) {
            super(position, priority);
            setSize(buttonPanelSize);
        }

        public String toString() {
            return "ButtonPanelArea";
        }
    }

}
