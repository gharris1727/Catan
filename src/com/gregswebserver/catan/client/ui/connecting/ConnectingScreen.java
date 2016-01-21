package com.gregswebserver.catan.client.ui.connecting;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.renderer.ClientScreen;

/**
 * Created by Greg on 1/18/2015.
 * A screen that is shown while the client is connecting to a remote server.
 */
public class ConnectingScreen extends ClientScreen {

    private final TiledBackground background;
    private final TextLabel text;

    public ConnectingScreen(Client client) {
        super(client);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            public String toString() {
                return "ConnectingScreenBackground";
            }
        };
        text = new TextLabel(1, UIStyle.FONT_HEADING, "Connecting...") {
            public String toString() {
                return "ConnectingScreenText";
            }
        };
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        text.getPosition().setLocation(getCenteredPosition(text.getGraphic().getMask()));
    }

    public String toString() {
        return "ConnectingScreen";
    }

    public void setProgress(int i) {

    }
}
