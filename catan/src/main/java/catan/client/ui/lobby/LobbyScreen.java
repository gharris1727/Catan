package catan.client.ui.lobby;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.UIConfig;
import catan.client.ui.ClientScreen;
import catan.common.structure.lobby.Lobby;

import java.awt.*;

/**
 * Created by greg on 1/10/16.
 * User Interface for when the user is in a lobby and preparing to join a game.
 */
public class LobbyScreen extends ClientScreen {

    private int buttonsHeight;

    private final Lobby lobby;

    private final LobbyUserList userList;
    private final LobbySettings settings;
    private final LobbyButtons buttons;

    public LobbyScreen(Lobby lobby) {
        super("LobbyScreen", "inlobby");
        this.lobby = lobby;
        settings = new LobbySettings();
        buttons = new LobbyButtons();
        userList = new LobbyUserList(lobby);
        add(settings);
        add(buttons);
        add(userList);
        update();
    }

    @Override
    public void update() {
        settings.setLobbyConfig(lobby.getConfig());
        userList.forceRender();
    }

    @Override
    public void loadConfig(UIConfig config) {
        buttonsHeight = config.getLayout().getInt("panel.height");
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
}
