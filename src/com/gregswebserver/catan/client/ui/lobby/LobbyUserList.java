package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
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

    public LobbyUserList(Lobby lobby) {
        super(2, "users");
        this.lobby = lobby;
        background = new EdgedTiledBackground(0, "background");
        add(background).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        clear();
        RenderMask userSize = new RoundedRectangularMask(getConfig().getLayout().getDimension("size"));
        int spacing = getConfig().getLayout().getInt("spacing");
        int height = spacing;
        for (Username user : lobby.getUsers()) {
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
            super(1, "element");
            background = new TiledBackground(0, "background");
            name = new TextLabel(1, "name", username.username);
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

        @Override
        public String toString() {
            return "LobbyUserListElement";
        }
    }

    @Override
    public String toString() {
        return "LobbyUserList";
    }
}
