package com.gregswebserver.catan.client.renderer.secondary.connected;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.common.lobby.ClientPool;

import java.awt.*;

/**
 * Created by greg on 1/10/16.
 * Sidebar of users on screen.
 */
public class UserListRegion extends UIScreenRegion {

    ScreenRegion background;
    private final ClientPool clients;

    public UserListRegion(int priority, UIStyle style, ClientPool clients) {
        super(priority, style);
        this.clients = clients;
        background = new TiledBackground(0, style.getBackgroundStyle()) {
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
