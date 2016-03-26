package com.gregswebserver.catan.client.ui.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.masks.RoundedRectangularMask;
import com.gregswebserver.catan.client.graphics.ui.*;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.event.MouseEvent;

/**
 * Created by greg on 1/23/16.
 * Sidebar for controlling lobby flow.
 */
public class LobbyButtons extends UIScreen {

    private final TiledBackground background;
    private final Button startButton;
    private final Button leaveButton;

    public LobbyButtons(LobbyScreen parent) {
        super(2, parent, "panel");
        //Load layout information
        //Create sub-regions
        background = new EdgedTiledBackground(0, UIStyle.BACKGROUND_INTERFACE);
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
        //Add everything to the screen
        add(background).setClickable(this);
        add(startButton);
        add(leaveButton);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
        startButton.setMask(new RoundedRectangularMask(getLayout().getDimension("start")));
        leaveButton.setMask(new RoundedRectangularMask(getLayout().getDimension("leave")));
    }

    @Override
    protected void renderContents() {
        int spacing = getLayout().getInt("spacing");
        center(startButton).y -= spacing;
        center(leaveButton).y += spacing;
    }

    @Override
    public String toString() {
        return "LobbyButtons";
    }
}
