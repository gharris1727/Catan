package com.gregswebserver.catan.client.ui.serverconnectmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.ui.primary.ServerPool;

/**
 * Created by Greg on 1/2/2015.
 * Server Connection Screen
 */
public class ServerConnectMenu extends UIScreenRegion {

    private final TiledBackground background;
    private final ServerListRegion servers;
    private final ServerEditRegion edit;

    public ServerConnectMenu(ServerPool list) {
        super(0);
        //Create the child regions
        servers = new ServerListRegion(1, list);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            public String toString() {
                return "ConnectMenuBackground";
            }
        };
        edit = new ServerEditRegion(0,null);
        //Add the regions to the render
        add(servers);
        add(background).setClickable(this);
    }

    @Override
    public void resizeContents(RenderMask mask) {
        //Resize the background region
        background.setMask(mask);
        //Resize the server display list
        servers.setMask(mask);
    }

    @Override
    protected void restyleContents(UIStyle style) {
        //Restyle the background region
        background.setStyle(style);
        //Restyle the server display list
        servers.setStyle(style);
    }

    public String toString() {
        return "ServerConnectMenu";
    }
}

