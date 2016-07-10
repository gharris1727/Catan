package catan.common.structure.event;

import catan.common.crypto.Username;
import catan.common.event.ExternalEvent;

/**
 * Created by greg on 1/27/16.
 * A class of events pertaining to the lobby and matchmaking system.
 */
public class LobbyEvent extends ExternalEvent<LobbyEventType> {

    public LobbyEvent(Username origin, LobbyEventType type, Object payload) {
        super(origin, type, payload);
    }
}
