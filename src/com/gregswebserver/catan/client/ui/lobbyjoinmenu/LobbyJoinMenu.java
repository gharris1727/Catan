package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;

import java.awt.*;

/**
 * Created by Greg on 1/17/2015.
 * The list of lobbies hosted on the current server.
 */
public class LobbyJoinMenu extends ClientScreen {

    private final LobbyListRegion lobbyList;
    private final UserListRegion userList;

    public LobbyJoinMenu(Client client) {
        super(client, "lobbylist");
        lobbyList = new LobbyListRegion(this, client.getLobbyList());
        userList = new UserListRegion(this, client.getClientList());
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
        int lobbyListWidth = (width*3)/4;
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

    @Override
    public void update() {
        lobbyList.update();
        userList.forceRender();
    }
}
