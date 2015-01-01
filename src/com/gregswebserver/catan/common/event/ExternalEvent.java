package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Event that is capable of being sent across the network, carries origin identity information.
 */
public abstract class ExternalEvent<T extends EventType> extends GenericEvent implements Serializable {

    public static final long serialVersionUID = 1L;

    protected final Identity origin;
    protected final T type;
    protected final Object payload;

    protected ExternalEvent(Identity origin, T type, Object payload) {
        this.origin = origin;
        this.type = type;
        this.payload = payload;
        //Check to make sure the payload is valid.
        //Prevents ClassCastExceptions later.
        type.checkPayload(payload);
        if (payload != null && !(payload instanceof Serializable))
            throw new EventPayloadException(payload, Serializable.class);
    }

    public Identity getOrigin() {
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
