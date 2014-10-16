package com.gregswebserver.catan.event;

/**
 * Created by Greg on 10/16/2014.
 * A generic interface defining an enum that is a type of event.
 */
public interface EventType {

    public void checkPayload(Object o);

    public Class getType();
}
