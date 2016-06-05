package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.lobby.ClientPool;

import java.awt.*;

/**
 * Created by greg on 1/10/16.
 * Sidebar of users on screen.
 */
public class UserListRegion extends ConfigurableScreenRegion implements Updatable {

    //Configuration dependencies
    private int userHeight;

    //Sub-regions
    private final TiledBackground background;
    private final UserList list;
    private final ScrollingScreenContainer container;

    public UserListRegion(ClientPool clients) {
        super("UserList", 1, "users");
        background = new TiledBackground();
        list = new UserList(clients);
        container = new ScrollingScreenContainer("UserScroll", 1, list);
        add(background).setClickable(this);
        add(container);
    }

    @Override
    public void loadConfig(UIConfig config) {
        userHeight = config.getLayout().getInt("height");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        container.setMask(mask);
    }

    @Override
    public void update() {
        container.update();
    }

    @Override
    protected void renderContents() {
        list.setElementSize(new RectangularMask(new Dimension(getMask().getWidth(), userHeight)));
    }

    private class UserList extends ScrollingList {

        private final ClientPool clients;

        private UserList(ClientPool clients) {
            super("UserList", 1, "list");
            this.clients = clients;
            setInsets(new Insets(0,0,0,0));
        }

        @Override
        public void update() {
            forceRender();
        }

        @Override
        protected void renderContents() {
            clear();
            for (Username username : clients) {
                add(new Element(username));
            }
            super.renderContents();
        }

        private class Element extends ConfigurableScreenRegion {

            private final TiledBackground background;
            private final TextLabel label;

            private Element(Username username) {
                super("Element", 0, "element");
                background = new TiledBackground();
                label = new TextLabel("UsernameLabel", 1, "label", username.username);
                add(background).setClickable(this);
                add(label).setClickable(this);
            }

            @Override
            protected void resizeContents(RenderMask mask) {
                background.setMask(mask);
            }

            @Override
            protected void renderContents() {
                center(label);
            }
        }
    }
}
