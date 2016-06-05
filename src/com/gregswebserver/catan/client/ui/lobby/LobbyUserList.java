package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.lobby.Lobby;

import java.awt.*;

/**
 * Created by greg on 1/20/16.
 * List of users in a lobby, with operations to manipulate the lobby
 */
public class LobbyUserList extends ConfigurableScreenRegion {

    private final Lobby lobby;
    private final TiledBackground background;
    private RenderMask userSize;
    private int spacing;

    public LobbyUserList(Lobby lobby) {
        super("LobbyUserList", 2, "users");
        this.lobby = lobby;
        background = new EdgedTiledBackground();
        add(background).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void loadConfig(UIConfig config) {
        userSize = new RoundedRectangularMask(config.getLayout().getDimension("size"));
        spacing = config.getLayout().getInt("spacing");
    }

    @Override
    protected void renderContents() {
        clear();
        int height = spacing;
        for (Username user : lobby.getPlayers()) {
            LobbyUserListElement elt = new LobbyUserListElement(user);
            add(elt).setPosition(new Point(0, height));
            elt.setConfig(getConfig());
            elt.setMask(userSize);
            center(elt).y = height;
            height += userSize.getHeight();
            height += spacing;
        }
        add(background);
    }

    private class LobbyUserListElement extends ConfigurableScreenRegion {

        private final TiledBackground background;
        private final TextLabel name;

        private LobbyUserListElement(Username username) {
            super("UserListElement", 1, "element");
            background = new TiledBackground();
            name = new TextLabel("UserListNameLabel", 1, "name", username.username);
            add(background).setClickable(this);
            add(name).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            background.setMask(mask);
        }

        @Override
        protected void renderContents() {
            center(name);
        }
    }
}
