package com.gregswebserver.catan.client.ui.lobbyjoinmenu;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.Button;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.event.MouseEvent;

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
        submitButton = new Button("CreateButton", 1, "submit", "Create new") {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Lobby_Create, null);
            }
        };
        add(background).setClickable(this);
        add(submitButton);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        center(submitButton).y = 64;
    }
}
