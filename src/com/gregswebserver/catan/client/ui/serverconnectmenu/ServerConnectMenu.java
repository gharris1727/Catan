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

/**
 * Created by Greg on 1/5/2015.
 * A list of servers printed on screen.
 */
public class ServerConnectMenu extends ClientScreen {

    private RenderMask serverSize;
    private RenderMask footerSize;
    private int padding;
    private int spacing;

    private final ServerPool serverPool;
    private ConnectionInfo selected;

    private final TiledBackground background;
    private final ScrollingScreenContainer scroll;
    private final ServerListFooter footer;

    private ServerDetailPopup editPopup;

    public ServerConnectMenu(ServerPool serverPool) {
        super("connect");
        //Store instance information
        this.serverPool = serverPool;
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
        return "ServerConnectMenu";
    }

    @Override
    public void update() {
        scroll.update();
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
            selected = null;
            int height = 0;
            for (ConnectionInfo elt : serverPool) {
                ServerListItem item = new ServerListItem(elt);
                item.setMask(serverSize);
                item.setConfig(getConfig());
                item.setPosition(new Point(0,height));
                add(item);
                height += serverSize.getHeight() + padding;
            }
            setMask(new RectangularMask(new Dimension(serverSize.getWidth(), height)));
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
        private final Button newButton;
        private final Button editButton;
        private final Button connectButton;
        private final TextBox passwordBox;

        private ServerListFooter() {
            super(2, "footer");
            background = new EdgedTiledBackground(0, "background");
            newButton = new Button(1, "new", "New") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return detail(null);
                }
                @Override
                public String toString() {
                    return "ServerListNewButton";
                }
            };
            editButton = new Button(1, "edit", "Edit") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return selected == null ? null : detail(selected);
                }
                @Override
                public String toString() {
                    return "ServerListEditButton";
                }
            };
            connectButton = new Button(1, "connect", "Connect") {
                @Override
                public String toString() {
                    return "ServerListFooterConnectButton";
                }
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return connect();
                }
            };
            passwordBox = new TextBox(1, "password", "Password", false) {
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
            add(newButton);
            add(editButton);
            add(connectButton);
            add(passwordBox);
        }

        private UserEvent detail(ConnectionInfo selected) {
            if (editPopup == null) {
                editPopup = new ServerDetailPopup(selected);
                return new UserEvent(this, UserEventType.Display_Popup, editPopup);
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

        public String toString() {
            return "ServerListFooter";
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
            super("serverdetail");
            this.server = server;
            background = new EdgedTiledBackground(0, "background");
            hostname = new TextBox(1, "hostname" ,(server == null) ? "Hostname" : server.getHostname(), server != null) {
                @Override
                protected UserEvent onAccept() {
                    return submit();
                }
                @Override
                public String toString() {
                    return "HostnameTextBox";
                }
            };
            port = new TextBox(1, "port" ,(server == null) ? "Port" : server.getPort(), server != null) {
                @Override
                protected UserEvent onAccept() {
                    return submit();
                }
                @Override
                public String toString() {
                    return "HostnameTextBox";
                }
            };
            username = new TextBox(1, "username" ,(server == null) ? "Username" : server.getUsername(), server != null) {
                @Override
                protected UserEvent onAccept() {
                    return submit();
                }
                @Override
                public String toString() {
                    return "HostnameTextBox";
                }
            };
            saveButton = new Button(1, "save", "Save") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return submit();
                }
                @Override
                public String toString() {
                    return "ServerDetailPopupCloseButton";
                }
            };
            deleteButton = new Button(1, "delete", "Delete") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    return delete();
                }
                @Override
                public String toString() {
                    return "ServerDetailPopupDeleteButton";
                }
            };
            cancelButton = new Button(1, "cancel", "Cancel") {
                @Override
                public UserEvent onMouseClick(MouseEvent event) {
                    expire();
                    return null;
                }
                @Override
                public String toString() {
                    return "ServerDetailPopupCancelButton";
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
            super.expire();
            editPopup = null;
        }

        private UserEvent delete() {
            expire();
            return new UserEvent(server, UserEventType.Server_Change, null);
        }

        private UserEvent submit() {
            expire();
            String hostnameText = hostname.getText();
            String portText = port.getText();
            String usernameText = username.getText();
            ConnectionInfo newInfo = new ConnectionInfo(hostnameText, portText, usernameText);
            if (!newInfo.equals(server) && hostnameText.length() > 0 && portText.length() > 0 && usernameText.length() >0) {
                return new UserEvent(server, UserEventType.Server_Change, newInfo);
            }
            return null;
        }

        @Override
        public void loadConfig(UIConfig config) {
            setMask(RenderMask.parseMask(config.getLayout().narrow("mask")));
        }

        @Override
        public String toString() {
            return "ServerDetailPopup";
        }
    }
}
