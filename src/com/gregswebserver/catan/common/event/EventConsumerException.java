package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 12/29/2014.
 * Exception thrown when an event consumer executes in invalid event.
 */
public class EventConsumerException extends Exception {

    public EventConsumerException(GenericEvent event) {
        super("Event consumer cannot process event: " + event);
    }
}
