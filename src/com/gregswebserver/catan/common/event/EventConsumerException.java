package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 12/29/2014.
 * Exception thrown when an event consumer executes in invalid event.
 */
public class EventConsumerException extends Exception {

    public EventConsumerException(String message, GenericEvent event) {
        super(message + ": " + event);
    }
}
