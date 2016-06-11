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
import com.gregswebserver.catan.client.ui.ClientScreen;
import com.gregswebserver.catan.client.ui.PopupWindow;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerConnectMenu extends ClientScreen {

    private RenderMask serverSize;
    private RenderMask footerSize;
    private int spacing;

    private final ServerPool serverPool;
    private ConnectionInfo selected;

    private final TiledBackground background;
    private final ScrollingScreenContainer scroll;
    private final ServerListFooter footer;

    private ServerDetailPopup editPopup;

    public ServerConnectMenu(ServerPool serverPool) {
        super("ConnectMenu", "connect");
        //Store instance information
        this.serverPool = serverPool;
        selected = null;
        //Create sub-regions
        background = new EdgedTiledBackground();
        scroll = new ScrollingScreenContainer("ServerScroll", 1, new ServerList());
        scroll.enableTransparency();
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
        serverSize = new RoundedRectangularMask(config.getLayout().getDimension("list.mask.size"));
        footerSize = new RoundedRectangularMask(config.getLayout().getDimension("footer"));
        spacing = config.getLayout().getInt("spacing");
    }

    @Override
    public void update() {
        scroll.update();
    }

    private class ServerList extends ScrollingList {

        private ServerList() {
            super("ServerList", 1, "list");
        }

        @Override
        public void loadConfig(UIConfig config) {
            setElementSize(RenderMask.parseMask(config.getLayout().narrow("mask")));
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
        public void update() {
            forceRender();
        }

        @Override
        protected void renderContents() {
            //Completely re-render all children
            clear();
            //TODO: selection logic should not depend on the rendering.
            selected = null;
            for (ConnectionInfo elt : serverPool)
                add(new Element(elt));
            super.renderContents();
        }

        private class Element extends ConfigurableScreenRegion {

            private final ConnectionInfo info;

            private final TiledBackground background;
            private final TextLabel address;
            private final TextLabel login;

            private Element(ConnectionInfo info) {
                super("Element", 1, "item");
                this.info = info;
                //Create all of the screen objects.
                background = new EdgedTiledBackground();
                address = new TextLabel("AddressText", 1, "address", "Remote Address : " + info.getHostname() + ":" + info.getPort());
                login = new TextLabel("LoginText", 2, "login", "Username: " + info.getUsername());
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
                return ServerList.this.onMouseScroll(event);
            }

            @Override
            public UserEvent onMouseDrag(Point p) {
                return ServerList.this.onMouseDrag(p);
            }
        }
    }

    private class ServerListFooter extends ConfigurableScreenRegion {

        private final TiledBackground background;
        private final Button newButton;
        private final Button editButton;
        private final Button connectButton;
        private final TextBox passwordBox;

        private ServerListFooter() {
            super("Footer", 2, "footer");
            background = new EdgedTiledBackground();
            newButton = new Button("NewButton", 1, "new", "New") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return detail(null);
                }
            };
            editButton = new Button("EditButton", 1, "edit", "Edit") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return selected == null ? null : detail(selected);
                }
            };
            connectButton = new Button("ConenctButton", 1, "connect", "Connect") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return connect();
                }
            };
            passwordBox = new TextBox("PasswordBox", 1, "password", "Password", false) {
                @Override
                public UserEvent onAccept() {
                    return connect();
                }
            };
            //Add the objects to the screen
            add(background).setClickable(this);
            add(newButton);
            add(editButton);
            add(connectButton);
            add(passwordBox);
        }

        private UserEvent detail(ConnectionInfo selected) {
            if (editPopup == null) {
                editPopup = new ServerDetailPopup(selected);
                editPopup.setConfig(getConfig());
                return editPopup.display();
            }
            return null;
        }

        private UserEvent connect() {
            if (selected != null) {
                serverPool.top(selected);
                //TODO: should this be here?
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
    }

    private class ServerDetailPopup extends PopupWindow {

        private final ConnectionInfo server;

        private final TiledBackground background;
        private final TextBox hostname;
        private final TextBox port;
        private final TextBox username;
        private final Button saveButton;
        private final Button deleteButton;
        private final Button cancelButton;

        private ServerDetailPopup(ConnectionInfo server) {
            super("ServerDetails", "serverdetail", ServerConnectMenu.this);
            this.server = server;
            background = new EdgedTiledBackground();
            hostname = new TextBox("HostnameBox", 1, "hostname" ,(server == null) ? "Hostname" : server.getHostname(), server != null) {
                @Override
                protected UserEvent onAccept() {
                    return submit();
                }
            };
            port = new TextBox("PortBox", 1, "port" ,(server == null) ? "Port" : server.getPort(), server != null) {
                @Override
                protected UserEvent onAccept() {
                    return submit();
                }
            };
            username = new TextBox("UsernameBox", 1, "username" ,(server == null) ? "Username" : server.getUsername(), server != null) {
                @Override
                protected UserEvent onAccept() {
                    return submit();
                }
            };
            saveButton = new Button("SaveButton", 1, "save", "Save") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return submit();
                }
            };
            deleteButton = new Button("DeleteButton", 1, "delete", "Delete") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return delete();
                }
            };
            cancelButton = new Button("CancelButton", 1, "cancel", "Cancel") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return expire();
                }
            };
            add(background).setClickable(this);
            add(hostname);
            add(port);
            add(username);
            add(saveButton);
            add(deleteButton);
            add(cancelButton);
        }

        @Override
        public UserEvent expire() {
            editPopup = null;
            return super.expire();
        }

        private UserEvent delete() {
            UserEvent remove = new UserEvent(this, UserEventType.Server_Remove, server);
            return new UserEvent(this, UserEventType.Composite_Event, Arrays.asList(remove, expire()));
        }

        private UserEvent submit() {
            String hostnameText = hostname.getText();
            String portText = port.getText();
            String usernameText = username.getText();
            ConnectionInfo newInfo = new ConnectionInfo(hostnameText, portText, usernameText);
            if (!newInfo.equals(server) && hostnameText.length() > 0 && portText.length() > 0 && usernameText.length() >0) {
                UserEvent remove = new UserEvent(this, UserEventType.Server_Remove, server);
                UserEvent add = new UserEvent(this, UserEventType.Server_Add, newInfo);
                return new UserEvent(this, UserEventType.Composite_Event, Arrays.asList(remove, add, expire()));
            }
            return expire();
        }

        @Override
        public void loadConfig(UIConfig config) {
            setMask(RenderMask.parseMask(config.getLayout().narrow("mask")));
        }

        @Override
        public void update() {
        }
    }
}
