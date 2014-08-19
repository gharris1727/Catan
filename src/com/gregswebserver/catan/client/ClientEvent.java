package com.gregswebserver.catan.client;

import com.gregswebserver.catan.event.InternalEvent;

/**
 * Created by Greg on 8/13/2014.
 * Created by the InputListener to control the Client itself
 * Handles operations such as connecting to the network.
 * Terminates at the Client, where the event queue is interpreted.
 */
public class ClientEvent extends InternalEvent {

    public final ClientEventType type;
    public final Object data;

    public ClientEvent(Object origin, ClientEventType type, Object data) {
        super(origin);
        this.type = type;
        this.data = data;
    }

    public String toString() {
        return "ClientEvent " + super.toString() + " Type: " + type + " Data: " + data;
    }
}
