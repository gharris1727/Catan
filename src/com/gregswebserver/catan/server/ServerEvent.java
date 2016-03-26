package com.gregswebserver.catan.server;

import com.gregswebserver.catan.common.event.InternalEvent;

/**
 * Created by Greg on 8/13/2014.
 * Server-side event
 */
public class ServerEvent extends InternalEvent<ServerEventType> {

    public ServerEvent(Object origin, ServerEventType type, Object payload) {
        super(origin, type, payload);
    }
}
