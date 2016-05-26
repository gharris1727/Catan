package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 12/29/2014.
 * Exception thrown when an event consumer executes in invalid event.
 */
public class EventConsumerException extends Exception {

    public EventConsumerException(String message, GenericEvent event, Throwable t) {
        super(message + (event != null ? ": " + event : ""), t);
    }

    public EventConsumerException(String message) {
        this(message, null, null);
    }

    public EventConsumerException(String message, GenericEvent event) {
        this(message, event, null);
    }

    public EventConsumerException(String message, Throwable t) {
        this(message, null, t);
    }

    public EventConsumerException(GenericEvent event, Throwable t) {
        this("Exception caused by", event, t);
    }
}
