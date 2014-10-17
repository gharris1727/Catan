package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.network.Identity;

import java.io.Serializable;

/**
 * Created by Greg on 8/12/2014.
 * Event that is capable of being sent across the network, carries origin identity information.
 */
public abstract class ExternalEvent<T extends EventType> extends GenericEvent<Identity, T> implements Serializable {

    protected ExternalEvent(Identity origin, T type, Object payload) {
        super(origin, type, payload);
        if (!(payload instanceof Serializable))
            throw new EventPayloadException(payload, Serializable.class);
    }
}
