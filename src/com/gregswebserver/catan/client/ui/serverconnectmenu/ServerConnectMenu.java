package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.ui.ClientScreen;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ServerConnectMenu extends ClientScreen {

    private final ServerListRegion servers;
    private final ServerEditRegion edit;

    public ServerConnectMenu(Client client) {
        super(client, "connect");
        //Create the child regions
        servers = new ServerListRegion(this, client.getServerList());
        edit = new ServerEditRegion(this, null);
        //Add the regions to the render
        add(servers);
    }

    @Override
    public void update() {

    }

    @Override
    public void resizeContents(RenderMask mask) {
        //Resize the server display list
        servers.setMask(mask);
    }

    public String toString() {
        return "ServerConnectMenu";
    }
}

