package com.gregswebserver.catan.client.ui.disconnecting;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.event.MouseEvent;

/**
 * Created by Greg on 1/17/2015.
 * A screen shown when the client disconnects for whatever reason.
 */
public class DisconnectingScreen extends ClientScreen {

    private final TiledBackground background;
    private final TextLabel text;
    private final Button button;
    private int spacing;

    public DisconnectingScreen(String disconnectMessage) {
        super("disconnecting");
        //Create subregions
        background = new EdgedTiledBackground(0, "background");
        text = new TextLabel(1, "message", disconnectMessage);
        button = new Button(2, "return", "Return to connect menu.") {
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
    public void update() { }

    @Override
    public void loadConfig(UIConfig config) {
        spacing = config.getLayout().getInt("spacing");
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
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
