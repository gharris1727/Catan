package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 8/12/2014.
 * Event that does not cross over a network connection under any condition. Used for events fired inside the application.
 */
public abstract class InternalEvent<T extends EventType> extends GenericEvent<Object, T> {

    public InternalEvent(Object origin, T type, Object payload) {
        super(origin, type, payload);
    }
}
