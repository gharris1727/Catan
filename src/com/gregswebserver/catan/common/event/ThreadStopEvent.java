package com.gregswebserver.catan.common.event;

/**
 * Created by Greg on 8/16/2014.
 * Event sent into the queue to signal that the event should commit sudoku.
 */
public class ThreadStopEvent extends GenericEvent<Object, EventType> {

    public ThreadStopEvent() {
        super(null, new ThreadStopEventType(), null);
    }

    private static class ThreadStopEventType implements EventType {

        public Class getType() {
            return null;
        }
    }
}
