package com.gregswebserver.catan.client.renderer.secondary.connected;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.common.lobby.MatchmakingPool;

import java.awt.*;

/**
 * Created by Greg on 1/17/2015.
 * The list of lobbies hosted on the current server.
 */
public class ServerScreenRegion extends UIScreenRegion {

    private final MatchmakingPool pool;

    private LobbyListRegion lobbyList;
    private UserListRegion userList;

    public ServerScreenRegion(UIStyle style, MatchmakingPool pool) {
        super(0, style);
        this.pool = pool;
        lobbyList = new LobbyListRegion(0, getStyle(), pool.getLobbyList());
        userList = new UserListRegion(1, getStyle(), pool.getUserList());
        //Add the screen contents
        add(lobbyList);
        add(userList);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        //Get the new overall size of the window.
        int width = mask.getWidth();
        int height = mask.getHeight();
        //Calculate intermediate dimensions
        int lobbyListWidth = (width*2)/3;
        int userListWidth = width - lobbyListWidth;
        //Create new positions
        lobbyList.setPosition(new Point());
        userList.setPosition(new Point(lobbyListWidth, 0));
        //Create new render masks.
        lobbyList.setMask(new RectangularMask(new Dimension(lobbyListWidth,height)));
        userList.setMask(new RectangularMask(new Dimension(userListWidth,height)));
    }

    public String toString() {
        return "ServerScreenRegion";
    }
}
