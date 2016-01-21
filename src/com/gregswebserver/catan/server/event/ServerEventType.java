package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.lobby.UserInfo;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Quit_All(null),//Kill all processes in the running server.
    Client_Disconnect(Username.class), //Removes a connection from the server after a disconnect.
    Client_Connect(UserInfo.class); //Connection has authenticated and confirmed the connection.

    private final Class payloadType;

    ServerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }

}
