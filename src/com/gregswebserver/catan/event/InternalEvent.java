package com.gregswebserver.catan.event;

/**
 * Created by Greg on 8/12/2014.
 * Event that does not cross over a network connection under any condition. Used for events fired inside the application.
 */
public abstract class InternalEvent extends GenericEvent {

    public InternalEvent(Object origin, EventType type, Object payload) {
        super(origin, type, payload);
    }

    public Object getOrigin() {
        return origin;
    }
}
