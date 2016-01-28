package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.renderer.ClientScreen;
import com.gregswebserver.catan.client.structure.LobbySettings;
import com.gregswebserver.catan.common.structure.Lobby;

import java.awt.*;

/**
 * Created by greg on 1/10/16.
 * User Interface for when the user is in a lobby and preparing to join a game.
 */
public class LobbyScreen extends ClientScreen {

    private static final int buttonsHeight = Client.staticConfig.getInt("catan.graphics.interface.inlobby.panel.height");

    private final LobbyUserList userList;
    private final LobbySettings settings;
    private final LobbyButtons buttons;

    public LobbyScreen(Client client) {
        super(client);
        settings = new LobbySettings(1);
        buttons = new LobbyButtons(2);
        userList = new LobbyUserList(2, client.getActiveLobby());
        add(settings);
        add(buttons);
        add(userList);
        update();
    }

    @Override
    public void update() {
        Lobby lobby = client.getActiveLobby();
        if (lobby != null)
            settings.setLobbyConfig(lobby.getConfig());
        settings.forceRender();
        userList.forceRender();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        int userListWidth = mask.getWidth()*2/3;
        int sidebarWidth = mask.getWidth() - userListWidth;
        int settingsHeight = mask.getHeight() - buttonsHeight;
        userList.setMask(new RectangularMask(new Dimension(userListWidth, mask.getHeight())));
        settings.setMask(new RectangularMask(new Dimension(sidebarWidth, settingsHeight)));
        buttons.setMask(new RectangularMask(new Dimension(sidebarWidth, buttonsHeight)));
        userList.setPosition(new Point());
        settings.setPosition(new Point(userListWidth, 0));
        buttons.setPosition(new Point(userListWidth, settingsHeight));
    }

    public String toString() {
        return "LobbyScreen";
    }
}
