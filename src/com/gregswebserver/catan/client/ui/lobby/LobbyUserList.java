package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.ui.UIScreen;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.lobby.Lobby;

import java.awt.*;

/**
 * Created by greg on 1/20/16.
 * List of users in a lobby, with operations to manipulate the lobby
 */
public class LobbyUserList extends UIScreen {

    private final Lobby lobby;
    private final TiledBackground background;

    public LobbyUserList(LobbyScreen parent, Lobby lobby) {
        super(2, parent, "users");
        this.lobby = lobby;
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW);
        add(background).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        clear();
        RenderMask userSize = new RoundedRectangularMask(getLayout().getDimension("size"));
        int spacing = getLayout().getInt("spacing");
        int height = spacing;
        for (Username user : lobby.getUsers()) {
            LobbyUserListElement elt = new LobbyUserListElement(user);
            add(elt).setPosition(new Point(0, height));
            elt.setStyle(getStyle());
            elt.setMask(userSize);
            center(elt).y = height;
            height += userSize.getHeight();
            height += spacing;
        }
        add(background);
    }

    private class LobbyUserListElement extends StyledScreenRegion {

        private final TiledBackground background;
        private final TextLabel name;

        private LobbyUserListElement(Username username) {
            super(1);
            background = new TiledBackground(0, UIStyle.BACKGROUND_USERS);
            name = new TextLabel(1,UIStyle.FONT_HEADING, username.username);
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
