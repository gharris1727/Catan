package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.renderer.ClientScreen;
import com.gregswebserver.catan.client.ui.lobbyjoinmenu.UserListRegion;

import java.awt.*;

/**
 * Created by greg on 1/10/16.
 * User Interface for when the user is in a lobby and preparing to join a game.
 */
public class LobbyScreen extends ClientScreen {

    private final TiledBackground background;
    private final UserListRegion allUsers;
    private final LobbyUserList lobbyUsers;

    public LobbyScreen(Client client) {
        super(client);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            @Override
            public String toString() {
                return "LobbyScreenBackground";
            }
        };
        allUsers = new UserListRegion(1, client.getClientList());
        lobbyUsers = new LobbyUserList(2, client.getActiveLobby(), client.getUsername());
        add(background).setClickable(this);
        add(allUsers);
        add(lobbyUsers);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        int allUsersWidth = mask.getWidth()*2/3;
        int lobbyUsersWidth = mask.getWidth() - allUsersWidth;
        allUsers.setMask(new RectangularMask(new Dimension(allUsersWidth,mask.getHeight())));
        lobbyUsers.setMask(new RectangularMask(new Dimension(lobbyUsersWidth,mask.getHeight())));
    }

    public String toString() {
        return "LobbyScreen";
    }

    public void update() {
        allUsers.forceRender();
        lobbyUsers.forceRender();
    }
}
