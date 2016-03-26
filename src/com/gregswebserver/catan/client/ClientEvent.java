package com.gregswebserver.catan.client;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by Greg on 8/13/2014.
 * Events that pertain to the client as a whole, and control the whole client's operation.
 */
public final class ClientEvent extends InternalEvent<ClientEventType> {

    public ClientEvent(Object origin, ClientEventType type, Object payload) {
        super(origin, type, payload);
    }
}
