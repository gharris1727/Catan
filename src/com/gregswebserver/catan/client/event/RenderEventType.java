package com.gregswebserver.catan.client.event;

import com.gregswebserver.catan.client.graphics.graphics.ScreenCanvas;
import com.gregswebserver.catan.common.crypto.ServerList;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.CatanGame;
import com.gregswebserver.catan.common.lobby.ClientPool;

/**
 * Created by Greg on 8/13/2014.
 * Enum sent with the RenderEvent indicating what is to be done with the data at the renderer.
 * General render interface information.
 * References are used to generate InputListener actions.
 */
public enum RenderEventType implements EventType {

    ConnectionListCreate(ServerList.class), // Disconnected
    ConnectProgress(Integer.class),
    LobbyListUpdate(ClientPool.class), // Connected
    DisconnectMessage(String.class),// Disconnecting
    GameCreate(CatanGame.class), //InGame
    Game_Update(null),
    Canvas_Update(ScreenCanvas.class),
    Animation_Step(null);

    private Class payloadType;

    RenderEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }
}
