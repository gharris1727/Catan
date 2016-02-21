package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.structure.lobby.ClientPool;

/**
 * Created by greg on 1/10/16.
 * Sidebar of users on screen.
 */
public class UserListRegion extends UIScreenRegion {

    private final TiledBackground background;
    private final ClientPool clients;

    public UserListRegion(int priority, ClientPool clients) {
        super(priority);
        this.clients = clients;
        background = new TiledBackground(0, UIStyle.BACKGROUND_USERS) {
            public String toString() {
                return "UserListBackground";
            }
        };
        add(background).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    public String toString() {
        return "UserListRegion";
    }
}
