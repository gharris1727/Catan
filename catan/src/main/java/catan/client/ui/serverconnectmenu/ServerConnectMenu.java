package catan.client.ui.serverconnectmenu;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.masks.RoundedRectangularMask;
import catan.client.graphics.ui.*;
import catan.client.input.UserEvent;
import catan.client.input.UserEventListener;
import catan.client.input.UserEventType;
import catan.client.structure.ConnectionInfo;
import catan.client.structure.ServerPool;
import catan.client.ui.ClientScreen;
import catan.client.ui.PopupWindow;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

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
        public void onMouseDrag(UserEventListener listener, Point p) {
            scroll(0, p.y);
        }

        @Override
        public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
            scroll(0, 4 * event.getUnitsToScroll());
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
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                selected = info;
            }

            @Override
            public void onMouseScroll(UserEventListener listener, MouseWheelEvent event) {
                ServerList.this.onMouseScroll(listener, event);
            }

            @Override
            public void onMouseDrag(UserEventListener listener, Point p) {
                ServerList.this.onMouseDrag(listener, p);
            }
        }
    }

    private class ServerListFooter extends ConfigurableScreenRegion {

        private final TiledBackground background;
        private final catan.client.graphics.ui.Button newButton;
        private final catan.client.graphics.ui.Button editButton;
        private final catan.client.graphics.ui.Button connectButton;
        private final TextBox passwordBox;

        private ServerListFooter() {
            super("Footer", 2, "footer");
            background = new EdgedTiledBackground();
            newButton = new catan.client.graphics.ui.Button("NewButton", 1, "new", "New") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    detail(null);
                }
            };
            editButton = new catan.client.graphics.ui.Button("EditButton", 1, "edit", "Edit") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    if (selected != null) detail(selected);
                }
            };
            connectButton = new catan.client.graphics.ui.Button("ConenctButton", 1, "connect", "Connect") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    connect(listener);
                }
            };
            passwordBox = new TextBox("PasswordBox", 1, "password", "Password", false) {
                @Override
                public void onAccept(UserEventListener listener) {
                    connect(listener);
                }
            };
            //Add the objects to the screen
            add(background).setClickable(this);
            add(newButton);
            add(editButton);
            add(connectButton);
            add(passwordBox);
        }

        private void detail(ConnectionInfo selected) {
            if (editPopup == null) {
                editPopup = new ServerDetailPopup(selected);
                editPopup.display();
            }
        }

        private void connect(UserEventListener listener) {
            if (selected != null) {
                serverPool.top(selected);
                //TODO: should this be here?
                scroll.update();
                selected.setPassword(passwordBox.getText());
                listener.onUserEvent(new UserEvent(this, UserEventType.Net_Connect, selected));
            }
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
        private final catan.client.graphics.ui.Button saveButton;
        private final catan.client.graphics.ui.Button deleteButton;
        private final catan.client.graphics.ui.Button cancelButton;

        private ServerDetailPopup(ConnectionInfo server) {
            super("ServerDetails", "serverdetail", ServerConnectMenu.this);
            this.server = server;
            background = new EdgedTiledBackground();
            hostname = new TextBox("HostnameBox", 1, "hostname" ,(server == null) ? "Hostname" : server.getHostname(), server != null) {
                @Override
                protected void onAccept(UserEventListener listener) {
                    submit(listener);
                }
            };
            port = new TextBox("PortBox", 1, "port" ,(server == null) ? "Port" : server.getPort(), server != null) {
                @Override
                protected void onAccept(UserEventListener listener) {
                    submit(listener);
                }
            };
            username = new TextBox("UsernameBox", 1, "username" ,(server == null) ? "Username" : server.getUsername(), server != null) {
                @Override
                protected void onAccept(UserEventListener listener) {
                    submit(listener);
                }
            };
            saveButton = new catan.client.graphics.ui.Button("SaveButton", 1, "save", "Save") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    submit(listener);
                }
            };
            deleteButton = new catan.client.graphics.ui.Button("DeleteButton", 1, "delete", "Delete") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    delete(listener);
                }
            };
            cancelButton = new catan.client.graphics.ui.Button("CancelButton", 1, "cancel", "Cancel") {
                @Override
                public void onMouseClick(UserEventListener listener, MouseEvent event) {
                    expire();
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
        public void expire() {
            editPopup = null;
            super.expire();
        }

        private void delete(UserEventListener listener) {
            listener.onUserEvent(new UserEvent(this, UserEventType.Server_Remove, server));
            expire();
        }

        private void submit(UserEventListener listener) {
            String hostnameText = hostname.getText();
            String portText = port.getText();
            String usernameText = username.getText();
            ConnectionInfo newInfo = new ConnectionInfo(hostnameText, portText, usernameText);
            if (!newInfo.equals(server) && hostnameText.length() > 0 && portText.length() > 0 && usernameText.length() >0) {
                listener.onUserEvent(new UserEvent(this, UserEventType.Server_Remove, server));
                listener.onUserEvent(new UserEvent(this, UserEventType.Server_Add, newInfo));
            }
            expire();
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
