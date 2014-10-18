package com.gregswebserver.catan.common.network;

import com.gregswebserver.catan.common.event.EventType;
import com.gregswebserver.catan.common.event.ExternalEvent;
import com.gregswebserver.catan.common.event.GenericEvent;

/**
 * Created by Greg on 8/12/2014.
 * Network event for communicating between client and server.
 */
public class NetEvent extends GenericEvent<NetID, NetEvent.NetEventType> {

    public final NetID origin;
    public final ExternalEvent event;

    public NetEvent(NetID origin, ExternalEvent event) {
        super(origin, null, event);
        this.origin = origin;
        this.event = event;
    }

    public class NetEventType implements EventType {
        public Class getType() {
            return ExternalEvent.class;
        }
    }
}
