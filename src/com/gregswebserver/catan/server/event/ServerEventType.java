package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.event.EventType;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Quit_All(null),//Kill all processes in the running server.
    Client_Disconnect(Integer.class); //Removes a connection from the server after a disconnect.

    private Class payloadType;

    ServerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }

}
