package com.gregswebserver.catan.client.renderer.primary.disconnected;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenObject;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.ui.primary.ConnectionInfo;
import com.gregswebserver.catan.client.ui.primary.ServerPool;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Iterator;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends UIScreenRegion {

    private static final int serverWidth = 384;
    private static final int serverHeight = 72;
    private static final RenderMask serverSize = new RoundedRectangularMask(new Dimension(serverWidth, serverHeight));
    private static final int buttonHeight = 96;
    private static final RenderMask footerSize = new RoundedRectangularMask(new Dimension(serverWidth, buttonHeight));
    private static final int listPadding = 4;
    private static final int vertPadding = 48;
    private final ServerPool list;

    private Point[] serverLocations;
    private Point footerLocation;

    //TODO: make scrolling continuous.
    private int scroll;
    private ConnectionInfo selected;

    ScreenRegion footer;

    public ServerListRegion(int priority, UIStyle style, ServerPool list) {
        super(priority, style);
        this.list = list;
        scroll = 0;
        selected = null;
        footer = new ServerListFooter(0);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        int listHeight = serverHeight + listPadding;
        //Find out the horizontal padding on either side.
        int hPadding = (width - serverWidth) / 2;
        //Find out how many servers we can display.
        int displayed = (height - vertPadding - buttonHeight) / serverHeight;
        //Figure out the vertical padding we need
        int vPadding = (height - listHeight * displayed - buttonHeight) / 2;
        //Fill out the array of servers.
        serverLocations = new Point[displayed];
        for (int i = 0; i < displayed; i++)
            serverLocations[i] = new Point(hPadding, vPadding + i * listHeight);
        footerLocation = new Point(hPadding, vPadding + displayed * listHeight);
    }

    @Override
    protected void renderContents() {
        clear();
        Iterator<ConnectionInfo> iter = list.iterator();
        for (int i = 0; i < serverLocations.length && i + scroll < list.size(); i++) {
            ScreenRegion item = new ServerListItem(0, iter.next());
            item.setMask(serverSize).setPosition(serverLocations[i]);
            add(item);
        }
        footer.setMask(footerSize).setPosition(footerLocation);
        add(footer);
    }

    public String toString() {
        return "ServerListArea";
    }

    private class ServerListItem extends ScreenRegion {

        private final ConnectionInfo info;

        private ScreenRegion background;
        private ScreenObject address;
        private ScreenObject login;

        public ServerListItem(int priority, ConnectionInfo info) {
            super(priority);
            this.info = info;
            //Create all of the screen objects.
            background = new EdgedTiledBackground(0, getStyle().getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            };
            address = new TextLabel(1, getStyle().getLightTextStyle(),
                    "Remote Address : " + info.getRemote() + ":" + info.getPort()) {
                public String toString() {
                    return "ServerListItem Remote Address";
                }
            };
            login = new TextLabel( 2, getStyle().getLightTextStyle(),
                    "Username: " + info.getUsername()) {
                public String toString() {
                    return "ServerListItem Username";
                }
            };
            //Add everything to the screen.
            add(login).setClickable(this);
            add(background).setClickable(this);
            add(address).setClickable(this);
            //Set the size
            setMask(serverSize);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            address.setPosition(new Point(16,16));
            login.setPosition(new Point(16,40));
        }

        @Override
        public UserEvent onMouseClick(MouseEvent event) {
            selected = info;
            return null;
        }

        @Override
        public String toString() {
            return "ServerListItemArea " + info;
        }
    }

    private class ServerListFooter extends ScreenRegion {

        private ScreenRegion background;
        private ScreenRegion connectButton;

        public ServerListFooter(int priority) {
            super(priority);
            background = new EdgedTiledBackground(0, getStyle().getInterfaceStyle()) {
                public String toString() {
                    return "ServerListItem Background";
                }
            };
            connectButton = new Button(1, getStyle(), "Connect") {
                public String toString() {
                    return "Connect Button";
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return (selected == null) ? null :new UserEvent(this, UserEventType.Net_Connect, selected);
                }
            };
            //Add the objects to the screen
            add(background).setClickable(this);
            add(connectButton);
            setMask(footerSize);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            connectButton.setMask(new RoundedRectangularMask(new Dimension(128, 32)));
            connectButton.setPosition(new Point(16,16));
        }

        public String toString() {
            return "ButtonPanelArea";
        }
    }

}
