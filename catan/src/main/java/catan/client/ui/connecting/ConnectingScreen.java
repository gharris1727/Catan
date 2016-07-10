package catan.client.ui.connecting;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.EdgedTiledBackground;
import catan.client.graphics.ui.TextLabel;
import catan.client.graphics.ui.TiledBackground;
import catan.client.ui.ClientScreen;

/**
 * Created by Greg on 1/18/2015.
 * A screen that is shown while the client is connecting to a remote server.
 */
public class ConnectingScreen extends ClientScreen {

    private final TiledBackground background;
    private final TextLabel text;

    public ConnectingScreen() {
        super("Connecting Screen", "connecting");
        background = new EdgedTiledBackground();
        text = new TextLabel("Connecting Progress", 1, "message", "Connecting...");
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
}
