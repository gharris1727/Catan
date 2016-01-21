package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.renderer.ClientScreen;

import java.awt.*;

/**
 * Created by Greg on 1/17/2015.
 * The list of lobbies hosted on the current server.
 */
public class LobbyJoinMenu extends ClientScreen {

    private final LobbyListRegion lobbyList;
    private final UserListRegion userList;

    public LobbyJoinMenu(Client client) {
        super(client);
        lobbyList = new LobbyListRegion(0, client.getLobbyList());
        userList = new UserListRegion(1, client.getClientList());
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
        return "LobbyJoinMenu";
    }
}
