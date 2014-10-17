package com.gregswebserver.catan.server.event;

import com.gregswebserver.catan.common.event.InternalEvent;
import com.gregswebserver.catan.common.network.Identity;

/**
 * Created by Greg on 8/13/2014.
 * Server-side event
 */
public class ServerEvent extends InternalEvent<ServerEventType> {

    public ServerEvent(Identity origin, ServerEventType type, Object payload) {
        super(origin, type, payload);
    }
}
