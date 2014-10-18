package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.network.ServerConnection;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Quit_All(null),
    Client_Connection(ServerConnection.class), //Fired when a client connects with Identity information.
    Client_Disconnection(ServerConnection.class); //Fired when a client disconnects.

    private Class payloadType;

    ServerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    public Class getType() {
        return payloadType;
    }

}
