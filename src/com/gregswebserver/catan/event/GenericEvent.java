package com.gregswebserver.catan.event;

/**
 * Created by Greg on 8/12/2014.
 * Event superclass extended by other custom events.
 */
public abstract class GenericEvent {

    protected final EventType type;
    protected final Object payload;
    protected final Object origin;

    public GenericEvent(Object origin, EventType type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        //Check to make sure the payload is valid.
        //Prevents ClassCastExceptions later.
        type.checkPayload(payload);
    }

    public abstract Object getOrigin();

    public abstract EventType getType();

    public Object getPayload() {
        return payload;
    }

    public String toString() {
        return "[EVENT] O:" + origin + " T:" + type + " P:" + payload;
    }
}
