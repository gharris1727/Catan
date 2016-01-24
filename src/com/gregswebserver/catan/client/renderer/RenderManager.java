package com.gregswebserver.catan.client.renderer;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphical;
import com.gregswebserver.catan.client.graphics.masks.Maskable;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.Renderable;
import com.gregswebserver.catan.client.graphics.ui.style.Styled;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.util.Animated;
import com.gregswebserver.catan.client.input.Clickable;
import com.gregswebserver.catan.client.ui.connecting.ConnectingScreen;
import com.gregswebserver.catan.client.ui.disconnecting.DisconnectingScreen;
import com.gregswebserver.catan.client.ui.lobby.LobbyScreen;
import com.gregswebserver.catan.client.ui.lobbyjoinmenu.LobbyJoinMenu;
import com.gregswebserver.catan.client.ui.serverconnectmenu.ServerConnectMenu;
import com.gregswebserver.catan.common.profiler.TimeSlice;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by greg on 1/15/16.
 * Function to manage the different ScreenObjects that need to be displayed by the Render Thread.
 */
public class RenderManager implements Renderable, Graphical, Animated, Clickable, Maskable, Styled {

    private final Client client;

    private RenderMask mask;
    private UIStyle style;

    public ServerConnectMenu serverConnectMenu;
    public ConnectingScreen connectingScreen;
    public DisconnectingScreen disconnectingScreen;
    public LobbyJoinMenu lobbyJoinMenu;
    public LobbyScreen lobbyScreen;

    private UIScreenRegion live;

    public RenderManager(Client client) {
        this.client = client;
        live = null;
    }

    public void displayServerConnectMenu() {
        serverConnectMenu = new ServerConnectMenu(client);
        display(serverConnectMenu);
    }

    public void displayServerConnectingScreen() {
        connectingScreen = new ConnectingScreen(client);
        display(connectingScreen);
    }

    public void displayServerDisconnectingScreen() {
        disconnectingScreen = new DisconnectingScreen(client);
        display(disconnectingScreen);
    }

    public void displayLobbyJoinMenu() {
        lobbyJoinMenu = new LobbyJoinMenu(client);
        display(lobbyJoinMenu);
    }

    public void displayInLobbyScreen() {
        lobbyScreen = new LobbyScreen(client);
        display(lobbyScreen);
    }

    private void display(UIScreenRegion region) {
        region.setStyle(style);
        region.setMask(mask);
        live = region;
    }

    @Override
    public void step() {
        if (live != null) live.step();
    }

    @Override
    public void reset() {
        if (live != null) live.reset();
    }

    @Override
    public String toString() {
        return "RenderManager";
    }

    @Override
    public void setMask(RenderMask mask) {
        this.mask = mask;
        if (live != null)
            live.setMask(mask);
    }

    @Override
    public RenderMask getMask() {
        return mask;
    }

    @Override
    public void forceRender() {
        if (live != null) live.forceRender();
    }

    @Override
    public boolean isRenderable() {
        return live != null && live.isRenderable();
    }

    @Override
    public TimeSlice getRenderTime() {
        return (live == null) ? null : live.getRenderTime();
    }

    @Override
    public Graphic getGraphic() {
        return (live == null) ? null : live.getGraphic();
    }

    @Override
    public UserEvent onMouseClick(MouseEvent event) {
        return (live == null) ? null : live.onMouseClick(event);
    }

    @Override
    public UserEvent onMousePress(MouseEvent event) {
        return (live == null) ? null : live.onMousePress(event);
    }

    @Override
    public UserEvent onMouseRelease(MouseEvent event) {
        return (live == null) ? null : live.onMouseRelease(event);
    }

    @Override
    public UserEvent onKeyTyped(KeyEvent event) {
        return (live == null) ? null : live.onKeyTyped(event);
    }

    @Override
    public UserEvent onKeyPressed(KeyEvent event) {
        return (live == null) ? null : live.onKeyTyped(event);
    }

    @Override
    public UserEvent onKeyReleased(KeyEvent event) {
        return (live == null) ? null : live.onKeyReleased(event);
    }

    @Override
    public UserEvent onMouseScroll(int rot) {
        return (live == null) ? null : live.onMouseScroll(rot);
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        return (live == null) ? null : live.onMouseDrag(p);
    }

    @Override
    public UserEvent onHover() {
        return (live == null) ? null : live.onHover();
    }

    @Override
    public UserEvent onUnHover() {
        return (live == null) ? null : live.onUnHover();
    }

    @Override
    public UserEvent onSelect() {
        return (live == null) ? null : live.onSelect();
    }

    @Override
    public UserEvent onDeselect() {
        return (live == null) ? null : live.onDeselect();
    }

    @Override
    public Clickable getClickable(Point p) {
        return (live == null) ? null : live.getClickable(p);
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
        if (live != null)
            live.setStyle(style);
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }
}
