package com.gregswebserver.catan.server;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 8/13/2014.
 * Server-side event
 */
public class ServerEvent extends ExternalEvent {

    public final ServerEventType type;
    public final Serializable data;

    public ServerEvent(Identity origin, ServerEventType type, Serializable data) {
        super(origin);
        this.type = type;
        this.data = data;
    }
}
