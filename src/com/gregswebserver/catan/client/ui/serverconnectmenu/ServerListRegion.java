package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;
import com.gregswebserver.catan.client.structure.ConnectionInfo;
import com.gregswebserver.catan.client.structure.ServerPool;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Iterator;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends ConfigurableScreenRegion {


    private RenderMask headerSize;
    private RenderMask serverSize;
    private RenderMask footerSize;
    private RenderMask paddingSize;

    private final ServerPool list;

    private Point[] serverLocations;

    //TODO: make scrolling continuous.
    private int scroll;
    private ConnectionInfo selected;

    private final TiledBackground background;
    private final ConfigurableScreenRegion footer;

    public ServerListRegion(ServerPool list) {
        super(0, "serverlist");
        //Load layout information
        //Store instance information
        this.list = list;
        scroll = 0;
        selected = null;
        //Create sub-regions
        background = new EdgedTiledBackground(0, "background");
        footer = new ServerListFooter();
        //Add everything to the screen.
        add(background).setClickable(this);
        add(footer);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //Resize the background region
        background.setMask(mask);
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
    public void loadConfig(UIConfig config) {
        headerSize = new RoundedRectangularMask(getConfig().getLayout().getDimension("header"));
        serverSize = new RoundedRectangularMask(getConfig().getLayout().getDimension("server"));
        footerSize = new RoundedRectangularMask(getConfig().getLayout().getDimension("footer"));
        paddingSize = new RoundedRectangularMask(getConfig().getLayout().getDimension("padding"));
    }

    @Override
    protected void renderContents() {
        //Completely re-render all children
        clear();
        Iterator<ConnectionInfo> iterator = list.iterator();
        for (int i = 0; i < serverLocations.length && i + scroll < list.size(); i++) {
            ServerListItem item = new ServerListItem(iterator.next());
            item.setMask(serverSize);
            item.setConfig(getConfig());
            item.setPosition(serverLocations[i]);
            add(item);
        }
        add(background);
        add(footer);
    }

    public String toString() {
        return "ServerListRegion";
    }

    private class ServerListItem extends ConfigurableScreenRegion {

        private final ConnectionInfo info;

        private final TiledBackground background;
        private final TextLabel address;
        private final TextLabel login;

        private ServerListItem(ConnectionInfo info) {
            super(1, "item");
            this.info = info;
            //Create all of the screen objects.
            background = new EdgedTiledBackground(0, "background");
            address = new TextLabel(1, "address", "Remote Address : " + info.getRemote() + ":" + info.getPort());
            login = new TextLabel(2, "login", "Username: " + info.getUsername());
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

    private class ServerListFooter extends ConfigurableScreenRegion {

        private final TiledBackground background;
        private final Button connectButton;
        private final TextBox passwordBox;

        private ServerListFooter() {
            super(2, "footer");
            background = new EdgedTiledBackground(0, "background");
            connectButton = new Button(1, "connect", "Connect") {
                public String toString() {
                    return "ServerListFooterConnectButton";
                }

                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return connect();
                }
            };
            passwordBox = new TextBox(1, "password", "Password") {
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
                selected.setPassword(passwordBox.getText());
                return new UserEvent(this, UserEventType.Net_Connect, selected);
            }
            return null;
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
            //TODO: move these to the layout config file.
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
