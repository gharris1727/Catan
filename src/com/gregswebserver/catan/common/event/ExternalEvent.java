package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.crypto.Username;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Event that is capable of being sent across the network, carries origin username information.
 */
public abstract class ExternalEvent<T extends EventType> extends GenericEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    private final Username origin;
    private final T type;
    private final Object payload;

    protected ExternalEvent(Username origin, T type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        //Check to make sure the payload is valid.
        //Prevents ClassCastExceptions later.
        type.checkPayload(payload);
        if (payload != null && !(payload instanceof Serializable))
            throw new EventPayloadException(payload, Serializable.class);
    }

    public Username getOrigin() {
        return origin;
    }

    public T getType() {
        return type;
    }

    public Object getPayload() {
        return payload;
    }

    public String toString() {
        return "[EXTERNAL] O(" + origin + ") T(" + type + ") P(" + payload + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalEvent)) return false;

        ExternalEvent<?> that = (ExternalEvent<?>) o;

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
