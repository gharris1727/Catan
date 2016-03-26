package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.structure.ConnectionInfo;

/**
 * Created by Greg on 1/5/2015.
 * A dialogue that pops up on the screen when a user wants to edit/create a new ServerPool entry.
 */
public class ServerEditRegion extends ConfigurableScreenRegion {

    private final ConnectionInfo info;

    public ServerEditRegion(ConnectionInfo info) {
        super(1, "edit");
        this.info = info;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    public String toString() {
        return "ServerEditDialogue " + info;
    }
}

