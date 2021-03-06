package catan.server;

import catan.common.event.InternalEvent;

/**
 * Created by Greg on 8/13/2014.
 * Server-side event
 */
public class ServerEvent extends InternalEvent<Object, ServerEventType> {

    public ServerEvent(Object origin, ServerEventType type, Object payload) {
        super(origin, type, payload);
    }
}
