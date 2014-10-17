package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 8/12/2014.
 * Event superclass extended by other custom events.
 */
public abstract class GenericEvent<O, T extends EventType> {

    protected final T type;
    protected final Object payload;
    protected final O origin;

    public GenericEvent(O origin, T type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        //Check to make sure the payload is valid.
        //Prevents ClassCastExceptions later.
        type.checkPayload(payload);
    }

    public O getOrigin() {
        return origin;
    }

    public T getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    public String toString() {
        return "[EVENT] O:" + origin + " T:" + type + " P:" + payload;
    }
}
