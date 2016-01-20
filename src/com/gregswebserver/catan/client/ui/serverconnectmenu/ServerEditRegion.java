package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.ui.primary.ConnectionInfo;

/**
 * Created by Greg on 1/5/2015.
 * A dialogue that pops up on the screen when a user wants to edit/create a new ServerPool entry.
 */
public class ServerEditRegion extends UIScreenRegion {

    ConnectionInfo info;

    public ServerEditRegion(int priority, ConnectionInfo info) {
        super(priority);
        this.info = info;
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    protected void restyleContents(UIStyle style) {
    }

    public String toString() {
        return "ServerEditDialogue " + info;
    }
}

