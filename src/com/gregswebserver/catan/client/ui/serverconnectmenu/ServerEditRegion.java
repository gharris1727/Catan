package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.structure.ConnectionInfo;
import com.gregswebserver.catan.client.ui.UIScreen;

/**
 * Created by Greg on 1/5/2015.
 * A dialogue that pops up on the screen when a user wants to edit/create a new ServerPool entry.
 */
public class ServerEditRegion extends UIScreen {

    private final ConnectionInfo info;

    public ServerEditRegion(ServerConnectMenu parent, ConnectionInfo info) {
        super(1, parent, "edit");
        this.info = info;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    public String toString() {
        return "ServerEditDialogue " + info;
    }
}

