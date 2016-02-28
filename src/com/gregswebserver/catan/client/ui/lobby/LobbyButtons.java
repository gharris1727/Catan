package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.Client;
import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.event.UserEventType;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.text.Button;
import com.gregswebserver.catan.client.graphics.ui.util.EdgedTiledBackground;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 1/23/16.
 * Sidebar for controlling lobby flow.
 */
class LobbyButtons extends UIScreenRegion {

    private static final RenderMask startSize = new RoundedRectangularMask(
            Client.graphicsConfig.getDimension("interface.inlobby.panel.start"));
    private static final RenderMask leaveSize = new RoundedRectangularMask(
            Client.graphicsConfig.getDimension("interface.inlobby.panel.leave"));
    private static final int spacing = Client.graphicsConfig.getInt("interface.inlobby.panel.spacing");

    private final TiledBackground background;
    private final Button startButton;
    private final Button leaveButton;

    public LobbyButtons(int priority) {
        super(priority);
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE) {
            @Override
            public String toString() {
                return "LobbySidebarBackground";
            }
        };
        startButton = new Button(1, "Start Game") {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Lobby_Start, null);
            }

            @Override
            public String toString() {
                return "LobbyButtonsStart";
            }
        };
        leaveButton = new Button(2, "Leave Lobby") {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Lobby_Quit, null);
            }

            @Override
            public String toString() {
                return "LobbyButtonsQuit";
            }
        };
        add(background).setClickable(this);
        add(startButton);
        add(leaveButton);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        startButton.setMask(startSize);
        leaveButton.setMask(leaveSize);
    }

    @Override
    protected void renderContents() {
        center(startButton).y -= spacing;
        center(leaveButton).y += spacing;
    }

    @Override
    public String toString() {
        return "LobbyButtons";
    }
}
