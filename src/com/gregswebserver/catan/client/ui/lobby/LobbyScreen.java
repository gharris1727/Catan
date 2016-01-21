package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.renderer.ClientScreen;
import com.gregswebserver.catan.client.ui.lobbyjoinmenu.UserListRegion;

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
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    public String toString() {
        return "LobbyScreen";
    }
}
