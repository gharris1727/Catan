package com.gregswebserver.catan.client.renderer.connecting;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;

/**
 * Created by Greg on 1/18/2015.
 * A screen that is shown while the client is connecting to a remote server.
 */
public class ConnectingScreen extends UIScreenRegion {

    private final TiledBackground background;
    private final TextLabel text;

    public ConnectingScreen() {
        super(0);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            public String toString() {
                return "DisconnectingScreen Background";
            }
        };
        text = new TextLabel(1, UIStyle.FONT_HEADING, "Connecting...") {
            public String toString() {
                return "Disconnect Reason";
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
    protected void restyleContents(UIStyle style) {
        background.setStyle(style);
        text.setStyle(style);
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
