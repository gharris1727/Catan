package com.gregswebserver.catan.client.ui.disconnecting;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.ui.ClientScreen;

import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/17/2015.
 * A screen shown when the client disconnects for whatever reason.
 */
public class DisconnectingScreen extends ClientScreen {

    private final TiledBackground background;
    private final TextLabel text;
    private final Button button;

    public DisconnectingScreen(Client client) {
        super(client, "disconnecting");
        //Create subregions
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_WINDOW);
        text = new TextLabel(1, UIStyle.FONT_HEADING, client.getDisconnectMessage());
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
        button.setMask(new RoundedRectangularMask(getLayout().getDimension("button.size")));
    }

    @Override
    protected void renderContents() {
        int spacing = getLayout().getInt("spacing");
        center(text).y -= spacing;
        center(button).y += spacing;
    }

    public String toString() {
        return "DisconnectingScreen";
    }
}
