package catan.common.event;

/**
 * Created by greg on 10/23/16.
 * A type of object that processes incoming events.
 */
public interface EventProcessor<T> {
    //Adds an object to the processing queue.
    void addEvent(T event);
}
