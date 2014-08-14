package com.gregswebserver.catan.server;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.network.Identity;

/**
 * Created by Greg on 8/13/2014.
 * Server-side event
 */
public class ServerEvent extends ExternalEvent {

    public final ServerEventType type;
    public final Object data;

    public ServerEvent(Identity origin, ServerEventType type, Object data) {
        super(origin);
        this.type = type;
        this.data = data;
    }
}
