package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

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
public class LobbyJoinMenu extends UIScreenRegion {

    private final MatchmakingPool pool;

    private LobbyListRegion lobbyList;
    private UserListRegion userList;

    public LobbyJoinMenu(MatchmakingPool pool) {
        super(0);
        this.pool = pool;
        lobbyList = new LobbyListRegion(0, pool.getLobbyList());
        userList = new UserListRegion(1, pool.getUserList());
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

    @Override
    protected void restyleContents(UIStyle style) {
        lobbyList.setStyle(style);
        userList.setStyle(style);
    }

    public String toString() {
        return "LobbyJoinMenu";
    }
}
