package com.gregswebserver.catan.common.event;

/**
 * Created by greg on 3/11/16.
 * A stateful, event consuming object that can have events undone.
 * A call to the undo() method should reverse the effects of the last successful execution of execute().
 * Any exception encountered during the undo process should be reported by an EventConsumerException.
 */
public interface ReversibleEventConsumer<T extends GenericEvent> extends EventConsumer<T> {

    void undo() throws EventConsumerException;
}
