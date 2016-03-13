package com.gregswebserver.catan.common.event;

/**
 * Created by greg on 3/11/16.
 * Interface for an event consumer that can step backwards in time.
 */
public interface ReversibleEventConsumer<T extends GenericEvent> extends EventConsumer<T> {

    void undo(T event) throws EventConsumerException;
}
