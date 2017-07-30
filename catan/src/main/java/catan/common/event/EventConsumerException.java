package catan.common.event;

/**
 * Created by Greg on 12/29/2014.
 * Exception thrown when an event consumer executes in invalid event.
 */
public class EventConsumerException extends Exception {

    private EventConsumerException(String message, GenericEvent event, Throwable t) {
        super((event == null) ? message : (message + ": " + event), t);
    }

    public EventConsumerException(GenericEvent event, EventConsumerProblem problem) {
        this(problem.getMessage(), event, null);
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
