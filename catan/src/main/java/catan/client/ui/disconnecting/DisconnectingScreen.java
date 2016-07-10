package catan.client.ui.disconnecting;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.*;
import catan.client.input.UserEvent;
import catan.client.input.UserEventListener;
import catan.client.input.UserEventType;
import catan.client.ui.ClientScreen;

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
        super("DisconnectingScreen", "disconnecting");
        //Create subregions
        background = new EdgedTiledBackground();
        text = new TextLabel("DisconnectMessageLabel", 1, "message", disconnectMessage);
        button = new Button("DisconnectButton", 2, "return", "Return to connect menu.") {
            @Override
            public void onMouseClick(UserEventListener listener, MouseEvent event) {
                listener.onUserEvent(new UserEvent(this, UserEventType.Net_Clear, null));
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
}
