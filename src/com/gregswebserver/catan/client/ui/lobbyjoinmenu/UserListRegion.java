package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.common.structure.lobby.ClientPool;

/**
 * Created by greg on 1/10/16.
 * Sidebar of users on screen.
 */
public class UserListRegion extends ConfigurableScreenRegion {

    private final TiledBackground background;
    private final ClientPool clients;

    public UserListRegion(ClientPool clients) {
        super(1, "users");
        this.clients = clients;
        background = new TiledBackground(0, "background");
        add(background).setClickable(this);
        //TODO: render the list of clients in a scrollable frame.
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    public String toString() {
        return "UserListRegion";
    }
}
