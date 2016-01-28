package com.gregswebserver.catan.client.ui.disconnecting;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.text.TextLabel;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;
import com.gregswebserver.catan.client.renderer.ClientScreen;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/17/2015.
 * A screen shown when the client disconnects for whatever reason.
 */
public class DisconnectingScreen extends ClientScreen {

    private final int spacing = Client.staticConfig.getInt("catan.graphics.interface.disconnecting.spacing");
    private final Dimension buttonSize = Client.staticConfig.getDimension("catan.graphics.interface.disconnecting.button.size");

    private final TiledBackground background;
    private final TextLabel text;
    private final Button button;

    public DisconnectingScreen(Client client) {
        super(client);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW) {
            public String toString() {
                return "DisconnectingScreenBackground";
            }
        };
        text = new TextLabel(1, UIStyle.FONT_HEADING, client.getDisconnectMessage()) {
            public String toString() {
                return "DisconnectingScreenDisconnectReason";
            }
        };
        button = new Button(2, "Return to connect screen.") {
            public String toString() {
                return "DisconnectingScreenDisconnectButton";
            }

            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Net_Clear, null);
            }
        };
        //Add the elements to the screen.
        add(background).setClickable(this);
        add(text).setClickable(this);
        add(button);
    }

    @Override
    public void update() {

    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        button.setMask(new RoundedRectangularMask(buttonSize));
    }

    @Override
    protected void renderContents() {
        center(text).y -= spacing;
        center(button).y += spacing;
    }

    public String toString() {
        return "DisconnectingScreen";
    }
}
