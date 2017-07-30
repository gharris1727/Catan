package catan.client.ui.lobby;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.masks.RoundedRectangularMask;
import catan.client.graphics.ui.*;
import catan.common.crypto.Username;
import catan.common.structure.lobby.Lobby;

import java.awt.*;

/**
 * Created by greg on 1/20/16.
 * List of users in a lobby, with operations to manipulate the lobby
 */
public class LobbyUserList extends ConfigurableScreenRegion {

    private final Lobby lobby;
    private final TiledBackground lobbyBackground;
    private RenderMask userSize;
    private int spacing;

    public LobbyUserList(Lobby lobby) {
        super("LobbyUserList", 2, "users");
        this.lobby = lobby;
        lobbyBackground = new EdgedTiledBackground();
        add(lobbyBackground).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        lobbyBackground.setMask(mask);
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
        add(lobbyBackground);
    }

    private class LobbyUserListElement extends ConfigurableScreenRegion {

        private final TiledBackground elementBackground;
        private final TextLabel usernameLabel;

        private LobbyUserListElement(Username username) {
            super("UserListElement", 1, "element");
            elementBackground = new TiledBackground();
            usernameLabel = new TextLabel("UserListNameLabel", 1, "name", username.username);
            add(elementBackground).setClickable(this);
            add(usernameLabel).setClickable(this);
        }

        @Override
        protected void resizeContents(RenderMask mask) {
            elementBackground.setMask(mask);
        }

        @Override
        protected void renderContents() {
            center(usernameLabel);
        }
    }
}
