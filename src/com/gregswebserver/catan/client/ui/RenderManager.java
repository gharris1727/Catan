package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.client.graphics.ui.Configurable;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;
import com.gregswebserver.catan.client.structure.GameManager;
import com.gregswebserver.catan.client.structure.ServerLogin;
import com.gregswebserver.catan.client.structure.ServerPool;
import com.gregswebserver.catan.client.ui.connecting.ConnectingScreen;
import com.gregswebserver.catan.client.ui.disconnecting.DisconnectingScreen;
import com.gregswebserver.catan.client.ui.ingame.InGameScreenRegion;
import com.gregswebserver.catan.client.ui.ingame.TeamColors;
import com.gregswebserver.catan.client.ui.lobby.LobbyScreen;
import com.gregswebserver.catan.client.ui.lobbyjoinmenu.LobbyJoinMenu;
import com.gregswebserver.catan.client.ui.serverconnectmenu.ServerConnectMenu;
import com.gregswebserver.catan.client.ui.serverdetail.ServerDetailMenu;
import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.lobby.Lobby;
import com.gregswebserver.catan.common.structure.lobby.MatchmakingPool;

import java.awt.*;

/**
 * Created by greg on 1/15/16.
 * Function to manage the different ScreenObjects that need to be displayed by the Render Thread.
 */
public class RenderManager extends ScreenRegion implements Configurable{

    private UIConfig config;
    private int taskbarHeight;
    private RenderMask liveMask;
    private Point livePosition;

    public ServerConnectMenu serverConnectMenu;
    public ServerDetailMenu serverDetailMenu;
    public ConnectingScreen connectingScreen;
    public DisconnectingScreen disconnectingScreen;
    public LobbyJoinMenu lobbyJoinMenu;
    public LobbyScreen lobbyScreen;
    public InGameScreenRegion gameScreen;

    private final TaskbarRegion taskbar;
    private ClientScreen live;

    public RenderManager() {
        super(0);
        taskbar = new TaskbarRegion();
        liveMask = new RectangularMask(new Dimension(0,0));
        livePosition = new Point();
        live = null;
        add(taskbar);
    }

    public void displayServerConnectMenu(ServerPool serverPool) {
        serverConnectMenu = new ServerConnectMenu(serverPool);
        display(serverConnectMenu);
    }

    public void displayServerDetailMenu(ServerLogin login) {
        serverDetailMenu = new ServerDetailMenu(login);
        display(serverDetailMenu);
    }

    public void displayServerConnectingScreen() {
        connectingScreen = new ConnectingScreen();
        display(connectingScreen);
    }

    public void displayServerDisconnectingScreen(String message) {
        disconnectingScreen = new DisconnectingScreen(message);
        display(disconnectingScreen);
    }

    public void displayLobbyJoinMenu(MatchmakingPool matchmakingPool) {
        lobbyJoinMenu = new LobbyJoinMenu(matchmakingPool);
        display(lobbyJoinMenu);
    }

    public void displayInLobbyScreen(Lobby lobby) {
        lobbyScreen = new LobbyScreen(lobby);
        display(lobbyScreen);
    }

    public void displayGameScreen(Username username, GameManager manager, TeamColors teamColors) {
        gameScreen = new InGameScreenRegion(username, manager, teamColors);
        display(gameScreen);
    }

    public void display(ClientScreen region) {
        if (live != null) {
            region.setConfig(getConfig());
            region.setMask(liveMask);
            region.setPosition(livePosition);
        }
        remove(live);
        live = region;
        add(live);
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
        display(live);
    }

    @Override
    public void setConfig(UIConfig config) {
        this.config = config;
        taskbarHeight = config.getLayout().getInt("taskbar.height");
        taskbar.setConfig(config);
        if (getMask() != null)
            resizeContents(getMask());
    }

    @Override
    public UIConfig getConfig() {
        return config;
    }
}
