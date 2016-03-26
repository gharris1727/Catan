package com.gregswebserver.catan.client.ui.connecting;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ClientScreen;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;

/**
 * Created by Greg on 1/18/2015.
 * A screen that is shown while the client is connecting to a remote server.
 */
public class ConnectingScreen extends ClientScreen {

    private final TiledBackground background;
    private final TextLabel text;

    public ConnectingScreen(Client client) {
        super(client, "connecting");
        background = new EdgedTiledBackground(0, "background");
        text = new TextLabel(1, "message", "Connecting...");
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    public void update() { }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        center(text);
    }

    public String toString() {
        return "ConnectingScreen";
    }
}
