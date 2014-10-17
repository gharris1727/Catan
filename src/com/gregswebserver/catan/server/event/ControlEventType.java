package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.event.EventPayloadException;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.game.gameplay.GameType;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 10/16/2014.
 * Type determining the purpose of a lobby event that controls repeating data between clients.
 */
public enum ControlEventType implements EventType {

    Client_Connect(null), //Fired when a client connects with Identity information.
    Client_Disconnect(null), //Fired when a client disconnects.
    Lobby_Create(Identity.class), //Fired when a lobby is created.
    Lobby_Delete(null), //Fired when a lobby is deleted.
    Lobby_Join(Identity.class), //Fired when a client joins a lobby.
    Lobby_Leave(null), //Fired when a client leaves a lobby, or is removed.
    Lobby_Update(null),
    Game_Start(GameType.class),
    Game_Quit(null),
    Game_End(null),
    Game_Replay(null),
    Replay_Start(null),
    Replay_Quit(null),
    Spectate_Start(Identity.class),
    Spectate_Quit(null);

    private Class payloadType;

    ControlEventType(Class payloadType) {
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
