package com.gregswebserver.catan.server;

import com.gregswebserver.catan.event.EventPayloadException;
import com.gregswebserver.catan.event.EventType;
import com.gregswebserver.catan.game.gameplay.GameType;
import com.gregswebserver.catan.server.lobby.Lobby;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Client_Connect(null), //Fired when a client connects with Identity information.
    Client_Disconnect(null), //Fired when a client disconnects.
    Lobby_Create(Lobby.class), //Fired when a lobby is created.
    Lobby_Delete(null), //Fired when a lobby is deleted.
    Lobby_Join(Lobby.class), //Fired when a client joins a lobby.
    Lobby_Leave(null), //Fired when a client leaves a lobby, or is removed.
    Lobby_Update(null),
    Game_Start(GameType.class),
    Game_End(null);

    private Class payloadType;

    ServerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public void checkPayload(Object o) {
        if (payloadType != null && o != null && o.getClass().isAssignableFrom(payloadType))
            throw new EventPayloadException(o, payloadType);
    }

    public Class getType() {
        return payloadType;
    }

}
