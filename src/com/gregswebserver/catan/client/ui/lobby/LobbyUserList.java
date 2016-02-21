package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.lobby.Lobby;

import java.awt.*;

/**
 * Created by greg on 1/20/16.
 * List of users in a lobby, with operations to manipulate the lobby
 */
public class LobbyUserList extends UIScreenRegion {

    private static final int spacing = Client.staticConfig.getInt("catan.graphics.interface.inlobby.users.spacing");

    private static final RenderMask userSize = new RoundedRectangularMask(
            Client.staticConfig.getDimension("catan.graphics.interface.inlobby.users"));

    private final Lobby lobby;
    private final TiledBackground background;

    public LobbyUserList(int priority, Lobby lobby) {
        super(priority);
        this.lobby = lobby;
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            @Override
            public String toString() {
                return "LobbyUserListBackground";
            }
        };
        add(background).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        clear();
        int height = spacing;
        for (Username user : lobby.getUsers()) {
            LobbyUserListElement elt = new LobbyUserListElement(1,user);
            add(elt).setPosition(new Point(0, height));
            elt.setStyle(getStyle());
            elt.setMask(userSize);
            center(elt).y = height;
            height += userSize.getHeight();
        }
        add(background);
    }

    private class LobbyUserListElement extends UIScreenRegion {

        private final TiledBackground background;
        private final TextLabel name;

        private LobbyUserListElement(int priority, Username username) {
            super(priority);
            background = new TiledBackground(0, UIStyle.BACKGROUND_USERS) {
                @Override
                public String toString() {
                    return "LobbyUserListElementBackground";
                }
            };
            name = new TextLabel(1,UIStyle.FONT_HEADING, username.username) {
                @Override
                public String toString() {
                    return "LobbyUserListElementNameLabel";
                }
            };
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
