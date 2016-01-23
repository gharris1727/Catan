package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.Main;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.text.TextBox;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.ui.primary.ConnectionInfo;
import com.gregswebserver.catan.client.ui.primary.ServerPool;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends UIScreenRegion {


    private static final RenderMask headerSize = new RoundedRectangularMask(Main.staticConfig.getDimension("catan.graphics.interface.serverlist.header"));
    private static final RenderMask serverSize = new RoundedRectangularMask(Main.staticConfig.getDimension("catan.graphics.interface.serverlist.server"));
    private static final RenderMask footerSize = new RoundedRectangularMask(Main.staticConfig.getDimension("catan.graphics.interface.serverlist.footer"));
    private static final RenderMask paddingSize = new RoundedRectangularMask(Main.staticConfig.getDimension("catan.graphics.interface.serverlist.padding"));

    private final ServerPool list;

    private Point[] serverLocations;

    //TODO: make scrolling continuous.
    private int scroll;
    private ConnectionInfo selected;

    private final UIScreenRegion footer;

    public ServerListRegion(int priority, ServerPool list) {
        super(priority);
        this.list = list;
        scroll = 0;
        selected = null;
        footer = new ServerListFooter(0);
        add(footer);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        int listHeight = serverSize.getHeight() + paddingSize.getHeight();
        //Find out the horizontal padding on either side.
        int hPadding = (width - serverSize.getWidth()) / 2;
        //Find out how many servers we can display.
        int displayed = (height - headerSize.getHeight() - footerSize.getHeight()) / serverSize.getHeight();
        //Figure out the vertical padding we need
        int vPadding = (height - listHeight * displayed - footerSize.getHeight()) / 2;
        //Fill out the array of servers.
        serverLocations = new Point[displayed];
        for (int i = 0; i < displayed; i++)
            serverLocations[i] = new Point(hPadding, vPadding + i * listHeight);
        //Resize the existing footer
        footer.setMask(footerSize);
        footer.setPosition(new Point(hPadding, vPadding + displayed * listHeight));
    }

    @Override
    protected void renderContents() {
        //Completely re-render all children
        clear();
        Iterator<ConnectionInfo> iterator = list.iterator();
        for (int i = 0; i < serverLocations.length && i + scroll < list.size(); i++) {
            ServerListItem item = new ServerListItem(0, iterator.next());
            item.setMask(serverSize);
            item.setStyle(getStyle());
            item.setPosition(serverLocations[i]);
            add(item);
        }
        add(footer);
    }

    public String toString() {
        return "ServerListRegion";
    }

    private class ServerListItem extends UIScreenRegion {

        private final ConnectionInfo info;

        private final TiledBackground background;
        private final TextLabel address;
        private final TextLabel login;

        public ServerListItem(int priority, ConnectionInfo info) {
            super(priority);
            this.info = info;
            //Create all of the screen objects.
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                public String toString() {
                    return "ServerListItemBackground";
                }
            };
            address = new TextLabel(1, UIStyle.FONT_PARAGRAPH,
                    "Remote Address : " + info.getRemote() + ":" + info.getPort()) {
                public String toString() {
                    return "ServerListItemRemoteAddress";
                }
            };
            login = new TextLabel(2, UIStyle.FONT_PARAGRAPH,
                    "Username: " + info.getUsername()) {
                public String toString() {
                    return "ServerListItemUsername";
                }
            };
            //Add everything to the screen.
            add(login).setClickable(this);
            add(background).setClickable(this);
            add(address).setClickable(this);
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

    private class ServerListFooter extends UIScreenRegion {

        private final TiledBackground background;
        private final Button connectButton;
        private final TextBox passwordBox;

        public ServerListFooter(int priority) {
            super(priority);
            background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
                public String toString() {
                    return "ServerListFooterBackground";
                }
            };
            connectButton = new Button(1, "Connect") {
                public String toString() {
                    return "ServerListFooterConnectButton";
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return connect();
                }
            };
            passwordBox = new TextBox(1) {
                @Override
                public UserEvent onAccept() {
                    return connect();
                }

                @Override
                public String toString() {
                    return "ServerListFooterPasswordBox";
                }
            };
            //Add the objects to the screen
            add(background).setClickable(this);
            add(connectButton);
            add(passwordBox);
        }

        private UserEvent connect() {
            if (selected != null) {
                selected.setPassword(passwordBox.getString());
                return new UserEvent(this, UserEventType.Net_Connect, selected);
            }
            return null;
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            connectButton.setMask(new RoundedRectangularMask(new Dimension(128, 32)));
            connectButton.setPosition(new Point(16, 16));
            passwordBox.setMask(new RectangularMask(new Dimension(200,32)));
            passwordBox.setPosition(new Point(150,16));
        }

        public String toString() {
            return "ServerListFooter";
        }
    }
}
