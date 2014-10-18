package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 10/16/2014.
 * A generic interface defining an enum that is a type of event.
 */
public interface EventType {

    default void checkPayload(Object o) {
        if (getType() != null && o != null && !getType().isInstance(o))
            throw new EventPayloadException(o, getType());
    }

    public Class getType();
}
