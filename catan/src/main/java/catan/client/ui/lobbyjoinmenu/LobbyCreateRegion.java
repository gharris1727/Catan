package catan.client.ui.lobbyjoinmenu;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.ui.Button;
import catan.client.graphics.ui.ConfigurableScreenRegion;
import catan.client.graphics.ui.EdgedTiledBackground;
import catan.client.graphics.ui.TiledBackground;
import catan.client.input.UserEvent;
import catan.client.input.UserEventType;

/**
 * Created by greg on 4/2/16.
 * Region to create a new lobby.
 */
public class LobbyCreateRegion extends ConfigurableScreenRegion {

    private final TiledBackground background;
    private final Button submitButton;

    public LobbyCreateRegion() {
        super("CreateLobby", 0, "create");
        background = new EdgedTiledBackground();
        submitButton = new Button("CreateButton", 1, "submit", "Create new", (listener) -> {
            listener.onUserEvent(new UserEvent(this, UserEventType.Lobby_Create, null));
        });
        add(background).setClickable(this);
        add(submitButton);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        center(submitButton).y = 64;
    }
}
