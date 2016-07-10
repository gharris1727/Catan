package catan.common.event;

/**
 * Created by Greg on 10/16/2014.
 * Exception thrown when the payload of an event does not match the definition.
 */
public class EventPayloadException extends Error {

    public EventPayloadException(Object payload, Class superClass) {
        super("Payload: " + payload + " was not an instance of " + superClass);
    }
}
