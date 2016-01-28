package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.crypto.Username;
import com.gregswebserver.catan.common.event.EventType;

import java.net.Socket;

/**
 * Created by Greg on 8/13/2014.
 * Several types of events generated as a ServerEvent.
 */
public enum ServerEventType implements EventType {

    Quit_All(null), //Kill all processes in the running server.
    Client_Connect(Socket.class),
    Client_Disconnect(Integer.class),
    User_Connect(Username.class),
    User_Disconnect(Username.class); //Removes a connection from the server after a logout.


    private final Class payloadType;

    ServerEventType(Class payloadType) {
        this.payloadType = payloadType;
    }

    @Override
    public Class getType() {
        return payloadType;
    }

}
