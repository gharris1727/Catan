package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.Configurable;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.structure.ServerPool;
import com.gregswebserver.catan.client.ui.connecting.ConnectingScreen;
import com.gregswebserver.catan.client.ui.disconnecting.DisconnectingScreen;
import com.gregswebserver.catan.client.ui.ingame.InGameScreenRegion;
import com.gregswebserver.catan.client.ui.ingame.TeamColors;
import com.gregswebserver.catan.client.ui.lobby.LobbyScreen;
import com.gregswebserver.catan.client.ui.lobbyjoinmenu.LobbyJoinMenu;
import com.gregswebserver.catan.client.ui.serverconnectmenu.ServerConnectMenu;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.lobby.Lobby;
import com.gregswebserver.catan.common.structure.lobby.MatchmakingPool;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by greg on 1/15/16.
 * Function to manage the different ScreenObjects that need to be displayed by the Render Thread.
 */
public class RenderManager extends ScreenRegion implements Configurable {

    private UIConfig config;
    private int taskbarHeight;
    private RenderMask liveMask;
    private Point livePosition;
    private Insets popupInsets;

    public ServerConnectMenu serverConnectMenu;
    public ConnectingScreen connectingScreen;
    public DisconnectingScreen disconnectingScreen;
    public LobbyJoinMenu lobbyJoinMenu;
    public LobbyScreen lobbyScreen;
    public InGameScreenRegion gameScreen;

    private final TaskbarRegion taskbar;
    private final List<PopupWindow> popups;
    private ClientScreen live;

    public RenderManager() {
        super(0);
        taskbar = new TaskbarRegion();
        live = null;
        popups = new LinkedList<>();
    }

    public void displayServerConnectMenu(ServerPool serverPool) {
        serverConnectMenu = new ServerConnectMenu(serverPool);
        displayScreen(serverConnectMenu);
    }

    public void displayServerConnectingScreen() {
        connectingScreen = new ConnectingScreen();
        displayScreen(connectingScreen);
    }

    public void displayServerDisconnectingScreen(String message) {
        disconnectingScreen = new DisconnectingScreen(message);
        displayScreen(disconnectingScreen);
    }

    public void displayLobbyJoinMenu(MatchmakingPool matchmakingPool) {
        lobbyJoinMenu = new LobbyJoinMenu(matchmakingPool);
        displayScreen(lobbyJoinMenu);
    }

    public void displayInLobbyScreen(Lobby lobby) {
        lobbyScreen = new LobbyScreen(lobby);
        displayScreen(lobbyScreen);
    }

    public void displayGameScreen(Username username, GameManager manager, TeamColors teamColors) {
        gameScreen = new InGameScreenRegion(username, manager, teamColors);
        displayScreen(gameScreen);
    }

    public void displayPopup(PopupWindow popup) {
        if (popup != null) {
            popup.setRenderManager(this);
            popup.setInsets(popupInsets);
            popup.setConfig(getConfig());
            center(popup);
            popups.add(popup);
            forceRender();
        }
    }

    public void removePopup(PopupWindow popup) {
        popups.remove(popup);
        forceRender();
    }

    public void displayScreen(ClientScreen region) {
        if (region != null) {
            if (getConfig() != null)
                region.setConfig(getConfig());
            if (liveMask != null) {
                region.setMask(liveMask);
                region.setPosition(livePosition);
            }
        }
        live = region;
        forceRender();
    }

    @Override
    protected void renderContents() {
        clear();
        add(taskbar);
        add(live);
        for (PopupWindow popup : popups)
            add(popup);
    }

    @Override
    public String toString() {
        return "RenderManager";
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        taskbar.setMask(new RectangularMask(new Dimension(mask.getWidth(), taskbarHeight)));
        liveMask = new RectangularMask(new Dimension(mask.getWidth(), mask.getHeight() - taskbarHeight));
        livePosition = new Point(0, taskbarHeight);
        popupInsets = new Insets(taskbarHeight, 0, 0, 0);
        if (live != null) {
            live.setMask(liveMask);
            live.setPosition(livePosition);
        }
    }

    @Override
    public void setConfig(UIConfig config) {
        this.config = config;
        taskbarHeight = config.getLayout().getInt("taskbar.height");
        taskbar.setConfig(config);
        if (live != null)
            live.setConfig(config);
    }

    @Override
    public UIConfig getConfig() {
        return config;
    }
}
