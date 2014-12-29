package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 8/12/2014.
 * Event that does not cross over a network connection under any condition. Used for events fired inside the application.
 */
public abstract class InternalEvent<T extends EventType> extends GenericEvent {

    protected final Object origin;
    protected final T type;
    protected final Object payload;

    public InternalEvent(Object origin, T type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        //Check to make sure the payload is valid.
        //Prevents ClassCastExceptions later.
        type.checkPayload(payload);
    }

    public Object getOrigin() {
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
