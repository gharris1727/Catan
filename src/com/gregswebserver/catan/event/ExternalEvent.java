package com.gregswebserver.catan.event;

import com.gregswebserver.catan.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Event that is capable of being sent across the network, carries origin identity information.
 */
public abstract class ExternalEvent extends GenericEvent implements Serializable {

    protected ExternalEvent(Identity origin, EventType type, Object payload) {
        super(origin, type, payload);
        if (!(payload instanceof Serializable))
            throw new EventPayloadException(payload, Serializable.class);
    }

    public Identity getOrigin() {
        return (Identity) origin;
    }
}
