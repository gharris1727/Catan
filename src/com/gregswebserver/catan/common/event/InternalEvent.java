package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 8/12/2014.
 * Event that does not cross over a network connection under any condition. Used for events fired inside the application.
 */
public abstract class InternalEvent<O, T extends EventType> extends GenericEvent {

    private final O origin;
    private final T type;
    private final Object payload;

    protected InternalEvent(O origin, T type, Object payload) {
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
        return "[INTERNAL] O(" + origin + ") T(" + type + ") P(" + payload + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InternalEvent)) return false;

        InternalEvent<?,?> that = (InternalEvent<?,?>) o;

        if (origin != null ? !origin.equals(that.origin) : that.origin != null) return false;
        if (!type.equals(that.type)) return false;
        return payload != null ? payload.equals(that.payload) : that.payload == null;

    }

    @Override
    public int hashCode() {
        int result = origin != null ? origin.hashCode() : 0;
        result = 31 * result + type.hashCode();
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        return result;
    }
}
