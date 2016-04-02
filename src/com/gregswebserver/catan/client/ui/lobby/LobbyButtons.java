package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 1/23/16.
 * Sidebar for controlling lobby flow.
 */
public class LobbyButtons extends ConfigurableScreenRegion {

    private final TiledBackground background;
    private final Button startButton;
    private final Button leaveButton;
    private int spacing;

    public LobbyButtons() {
        super(2, "panel");
        //Create sub-regions
        background = new EdgedTiledBackground(0, "background");
        startButton = new Button(1, "start", "Start Game") {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Lobby_Start, null);
            }

            @Override
            public String toString() {
                return "LobbyButtonsStart";
            }
        };
        leaveButton = new Button(2, "leave", "Leave Lobby") {
            @Override
            public UserEvent onMouseClick(MouseEvent event) {
                return new UserEvent(this, UserEventType.Lobby_Quit, null);
            }

            @Override
            public String toString() {
                return "LobbyButtonsQuit";
            }
        };
        //Add everything to the screen
        add(background).setClickable(this);
        add(startButton);
        add(leaveButton);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void loadConfig(UIConfig config) {
        spacing = config.getLayout().getInt("spacing");
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
