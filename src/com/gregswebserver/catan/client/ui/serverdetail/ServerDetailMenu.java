package com.gregswebserver.catan.client.ui.serverdetail;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.client.structure.ServerLogin;

/**
 * Created by greg on 4/2/16.
 * Detailed creation and edit screen for server login details.
 */
public class ServerDetailMenu extends ClientScreen {

    //TODO: provide fields for editing the server connection details.

    private final TiledBackground background;

    public ServerDetailMenu(ServerLogin login) {
        super("serverdetail");
        background = new EdgedTiledBackground(0, "background");
        add(background).setClickable(this);
    }

    @Override
    public void update() {
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public String toString() {
        return "ServerDetailMenu";
    }
}
