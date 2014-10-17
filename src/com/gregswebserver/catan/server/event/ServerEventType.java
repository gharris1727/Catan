package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.event.EventPayloadException;
import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.network.ClientConnection;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Quit_All(null),
    Client_Connection(ClientConnection.class), //Fired when a client connects with Identity information.
    Client_Disconnection(ClientConnection.class); //Fired when a client disconnects.

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
