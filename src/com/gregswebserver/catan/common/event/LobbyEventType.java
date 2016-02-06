package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.structure.LobbyConfig;
import com.gregswebserver.catan.common.structure.UserInfo;

/**
 * Created by greg on 1/27/16.
 * The payload type for a lobby event.
 */
public enum LobbyEventType implements EventType {

    User_Connect(UserInfo.class), //Server -> Broadcast, when a user logs in.
    User_Disconnect(null), //Client -> Server -> Broadcast, when a user logs out.
    Lobby_Create(LobbyConfig.class), //Client -> Server -> Broadcast, when a lobby is created.
    Lobby_Change_Config(LobbyConfig.class), //Client -> Server -> Broadcast, when a lobby is modified.
    Lobby_Join(Username.class), //Client -> Server -> Broadcast, when a client joins a lobby, stores owner of lobby.
    Lobby_Leave(null); //Client -> Server -> Broadcast, when a client leaves a lobby.

    private final Class payloadType;

    LobbyEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }
}
