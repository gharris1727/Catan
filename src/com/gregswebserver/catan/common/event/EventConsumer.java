package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 12/29/2014.
 * An object that can consume events. Offers mechanism for testing validity beforehand.
 */
public interface EventConsumer<T extends GenericEvent> {

    boolean test(T event);

    void execute(T event) throws EventConsumerException;

}
