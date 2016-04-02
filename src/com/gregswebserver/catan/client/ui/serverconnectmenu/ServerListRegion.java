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
import java.awt.event.MouseWheelEvent;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerListRegion extends ConfigurableScreenRegion {

    private RenderMask serverSize;
    private RenderMask footerSize;
    private int padding;
    private int spacing;

    private final ServerPool list;
    private ConnectionInfo selected;

    private final TiledBackground background;
    private final ScrollingScreenContainer scroll;
    private final ServerListFooter footer;

    public ServerListRegion(ServerPool list) {
        super(0, "serverlist");
        //Store instance information
        this.list = list;
        selected = null;
        //Create sub-regions
        background = new EdgedTiledBackground(0, "background");
        scroll = new ServerListScrollContainer(new ServerListScroll());
        footer = new ServerListFooter();
        //Add everything to the screen.
        add(background).setClickable(this);
        add(scroll);
        add(footer);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //Resize the background region
        background.setMask(mask);
        //Resize the server list scroll.
        Dimension scrollSize = new Dimension(serverSize.getWidth(), mask.getHeight() - footerSize.getHeight() - 2*spacing);
        scroll.setMask(new RectangularMask(scrollSize));
        scroll.setInsets(new Insets(0,0,0,0));
        //Resize the existing footer
        footer.setMask(footerSize);
    }

    @Override
    protected void renderContents() {
        center(scroll).y = spacing;
        center(footer).y = getMask().getHeight() - footerSize.getHeight();
    }

    @Override
    public void loadConfig(UIConfig config) {
        serverSize = new RoundedRectangularMask(config.getLayout().getDimension("server"));
        footerSize = new RoundedRectangularMask(config.getLayout().getDimension("footer"));
        padding = config.getLayout().getInt("padding");
        spacing = config.getLayout().getInt("spacing");
    }

    public String toString() {
        return "ServerListRegion";
    }

    private class ServerListScrollContainer extends ScrollingScreenContainer {

        private ServerListScrollContainer(ScrollingScreenRegion scroll) {
            super(1, "container", scroll);
            setTransparency(true);
        }

        @Override
        public String toString() {
            return "ServerListScrollContainer";
        }
    }

    private class ServerListScroll extends ScrollingScreenRegion {

        private ServerListScroll() {
            super(1, "scroll");
        }

        @Override
        public String toString() {
            return "ServerListScroll";
        }

        @Override
        public UserEvent onMouseDrag(Point p) {
            scroll(0, p.y);
            return null;
        }

        @Override
        public UserEvent onMouseScroll(MouseWheelEvent event) {
            scroll(0, 4 * event.getUnitsToScroll());
            return null;
        }

        @Override
        protected void renderContents() {
            //Completely re-render all children
            clear();
            int height = 0;
            for (ConnectionInfo elt : list) {
                ServerListItem item = new ServerListItem(elt);
                item.setMask(serverSize);
                item.setConfig(getConfig());
                item.setPosition(new Point(0,height));
                add(item);
                height += serverSize.getHeight() + padding;
            }
            if (height == 0) {
                setMask(serverSize);
                setTransparency(true);
            } else {
                setTransparency(false);
                setMask(new RectangularMask(new Dimension(serverSize.getWidth(), height)));
            }
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
                address = new TextLabel(1, "address", "Remote Address : " + info.getHostname() + ":" + info.getPort());
                login = new TextLabel(2, "login", "Username: " + info.getUsername());
                //Add everything to the screen.
                add(login).setClickable(this);
                add(background).setClickable(this);
                add(address).setClickable(this);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
                address.setPosition(new Point(16, 16));
                login.setPosition(new Point(16, 40));
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                selected = info;
                return null;
            }

            @Override
            public UserEvent onMouseScroll(MouseWheelEvent event) {
                return ServerListScroll.this.onMouseScroll(event);
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return ServerListScroll.this.onMouseDrag(p);
            }

            @Override
            public String toString() {
                return "ServerListItemArea " + info;
            }
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
                list.top(selected);
                scroll.update();
                selected.setPassword(passwordBox.getText());
                return new UserEvent(this, UserEventType.Net_Connect, selected);
            }
            return null;
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        public String toString() {
            return "ServerListFooter";
        }
    }
}
