package com.gregswebserver.catan.client.renderer.connect;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.TextGraphic;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.screen.StaticObject;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.common.crypto.ConnectionInfo;
import com.gregswebserver.catan.common.crypto.ServerList;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends UIScreenRegion {

    private static final int serverWidth = 384;
    private static final int serverHeight = 72;
    private static final RenderMask serverSize = new RoundedRectangularMask(new Dimension(serverWidth, serverHeight));
    private static final int buttonHeight = 96;
    private static final RenderMask buttonPanelSize = new RoundedRectangularMask(new Dimension(serverWidth, buttonHeight));
    private static final int listPadding = 4;
    private static final int vertPadding = 48;
    private final ServerList list;

    private Point[] serverLocations;
    private Point panelLocation;

    private int scroll;
    private int selected;

    public ServerListRegion(Point position, int priority, RenderMask mask, UIStyle style, ServerList list) {
        super(position, priority, mask, style);
        this.list = list;
        scroll = 0;
        selected = -1;
    }

    protected void resizeContents(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        int listHeight = serverHeight + listPadding;
        //Find out the horizontal padding on either side.
        int hPadding = (width - serverWidth) / 2;
        //Find out how many servers we can display.
        int displayed = ((height - vertPadding - buttonHeight) / serverHeight) - 1;
        //Figure out the vertical padding we need
        int vPadding = (height - listHeight * displayed - buttonHeight) / 2;
        //Fill out the array of servers.
        serverLocations = new Point[displayed];
        for (int i = 0; i < displayed; i++)
            serverLocations[i] = new Point(hPadding, vPadding + i * listHeight);
        panelLocation = new Point(hPadding, vPadding + displayed * listHeight);
    }

    protected void renderContents() {
        clear();
        for (int i = 0; i < serverLocations.length && i + scroll < list.size(); i++)
            add(new ServerListItem(serverLocations[i], 0, i + scroll, serverSize));
        add(new ButtonPanelAreaScreen(panelLocation, 0, buttonPanelSize));
    }

    public String toString() {
        return "ServerListArea";
    }

    private class ServerListItem extends ScreenRegion {

        private final int listIndex;

        public ServerListItem(Point position, int priority, int listIndex, RenderMask mask) {
            super(position, priority, mask);
            this.listIndex = listIndex;
        }

        protected void renderContents() {
            clear();

            ConnectionInfo info = list.get(listIndex);
            String address = "Remote Address : " + info.getRemote() + ":" + info.getPort();
            Graphic addressGraphic = new TextGraphic(getStyle().getLightTextStyle(), address);
            String login = "Username: " + info.getUsername();
            Graphic loginGraphic = new TextGraphic(getStyle().getLightTextStyle(), login);

            add(new EdgedTiledBackground(new Point(), 0, getMask(), getStyle().getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            }).setClickable(this);
            add(new StaticObject(new Point(16, 16), 1, addressGraphic) {
                public String toString() {
                    return "ServerListItem Remote Address";
                }
            }).setClickable(this);
            add(new StaticObject(new Point(16, 40), 2, loginGraphic) {
                public String toString() {
                    return "ServerListItem Username";
                }
            }).setClickable(this);
        }

        public UserEvent onMouseClick(MouseEvent event) {
            selected = listIndex;
            return null;
        }

        public String toString() {
            return "ServerListItemArea " + listIndex;
        }
    }

    private class ButtonPanelAreaScreen extends ScreenRegion {

        public ButtonPanelAreaScreen(Point position, int priority, RenderMask mask) {
            super(position, priority, mask);
        }

        protected void renderContents() {
            clear();
            add(new EdgedTiledBackground(new Point(), 0, getMask(), getStyle().getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            }).setClickable(this);
            add(new Button(new Point(16, 16), 1, new RoundedRectangularMask(new Dimension(128, 32)), getStyle(), "Connect") {
                public String toString() {
                    return "Connect Button";
                }

                public UserEvent onMouseClick(MouseEvent event) {
                    if (selected >= 0 && selected < list.size())
                        return new UserEvent(this, UserEventType.Net_Connect, list.get(selected));
                    return null;
                }
            });
        }

        public String toString() {
            return "ButtonPanelArea";
        }
    }

}
