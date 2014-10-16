package com.gregswebserver.catan.client;

import com.gregswebserver.catan.event.InternalEvent;

/**
 * Created by Greg on 8/13/2014.
 * Created by the InputListener to control the Client itself
 * Handles operations such as connecting to the network.
 * Terminates at the Client, where the event queue is interpreted.
 */
public class ClientEvent extends InternalEvent {

    public ClientEvent(Object origin, ClientEventType type, Object payload) {
        super(origin, type, payload);
    }

    public ClientEventType getType() {
        return (ClientEventType) type;
    }
}
