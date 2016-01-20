package com.gregswebserver.catan.client.renderer.lobby;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.common.lobby.Lobby;

/**
 * Created by greg on 1/10/16.
 * User Interface for when the user is in a lobby and preparing to join a game.
 */
public class InLobbyScreenRegion extends UIScreenRegion {

    public InLobbyScreenRegion(Lobby lobby) {
        super(0);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }

    @Override
    protected void restyleContents(UIStyle style) {
    }

    public String toString() {
        return "InLobbyScreenRegion";
    }
}
