package com.gregswebserver.catan.network;

import com.gregswebserver.catan.event.ExternalEvent;
import com.gregswebserver.catan.event.GenericEvent;

/**
 * Created by Greg on 8/12/2014.
 * Network event for communicating between client and server.
 */
public class NetEvent extends GenericEvent {

    public final NetID origin;
    public final ExternalEvent event;

    public NetEvent(NetID origin, ExternalEvent event) {
        this.origin = origin;
        this.event = event;
    }

    public ExternalEvent getEvent() {
        return event;
    }
}
