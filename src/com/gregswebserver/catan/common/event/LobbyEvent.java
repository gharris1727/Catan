package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.Username;

/**
 * Created by greg on 1/27/16.
 * A class of events pertaining to the lobby and matchmaking system.
 */
public class LobbyEvent extends ExternalEvent<LobbyEventType> {

    public LobbyEvent(Username origin, LobbyEventType type, Object payload) {
        super(origin, type, payload);
    }
}
