package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.screen.ColorScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.GridScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.TextScreenRegion;
import com.gregswebserver.catan.client.resources.FontInfo;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends GridScreenRegion {

    private static final Dimension serverSize = new Dimension(256, 128);
    private static final Dimension buttonPanelSize = new Dimension(serverSize.width, 256);
    private static final int vertPadding = 128;
    private final ServerList list;
    private int scroll;
    private int displayed;
    private int selected;

    public ServerListRegion(Point position, int priority, Dimension size, ServerList list) {
        super(position, priority);
        setSize(size);
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
        setGridSize(widths, heights);
    }

    protected void render() {
        clear();
        for (int i = 0; i < displayed; i++)
            add(new ServerListItem(new Point(1, i + 1), 0, i + scroll));
        add(new ButtonPanelAreaScreen(new Point(1, displayed), 0));
    }

    public String toString() {
        return "ServerListArea";
    }

    private class ServerListItem extends ColorScreenRegion {

        private final int listIndex;

        public ServerListItem(Point position, int priority, int listIndex) {
            super(position, priority, serverSize);
            this.listIndex = listIndex;
            ConnectionInfo info = list.get(listIndex);
            add(new TextScreenRegion(new Point(), 0, ResourceLoader.getFont(FontInfo.Default), "Remote Address: " + info.getRemote() + ":" + info.getPort()) {
                public String toString() {
                    return "ServerListItem Remote Address";
                }
            });
            add(new TextScreenRegion(new Point(0, 64), 0, ResourceLoader.getFont(FontInfo.Default), "Username: " + info.getUsername()) {
                public String toString() {
                    return "ServerListItem Username";
                }
            });
        }

        public UserEvent onMouseClick(MouseEvent event) {
            selected = listIndex;
            return null;
        }

        public String toString() {
            return "ServerListItemArea " + listIndex;
        }
    }

    private class ButtonPanelAreaScreen extends ColorScreenRegion {

        public ButtonPanelAreaScreen(Point position, int priority) {
            super(position, priority, buttonPanelSize);
        }

        public String toString() {
            return "ButtonPanelArea";
        }
    }

}
