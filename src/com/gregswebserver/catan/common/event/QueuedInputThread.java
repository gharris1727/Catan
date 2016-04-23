package com.gregswebserver.catan.common.event;

import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Greg on 8/12/2014.
 * Generic for a class that has an event queue on the frontend accepting events, and a event to process and dispatch them.
 * Calls the execute() function repeatedly, but does not necessarily have to block for events.
 * execute() will continue looping until stop() is called and running is set to false.
 */
public abstract class QueuedInputThread<T extends GenericEvent> {

    public final Logger logger;
    private final LinkedBlockingQueue<T> eventQueue;
    private final Thread run;
    private boolean running;

    protected QueuedInputThread(Logger logger) {
        this.logger = logger;
        eventQueue = new LinkedBlockingQueue<>();
        run = new Thread(toString()) {
            @Override
            public void run() {
                while (running) {
                    try {
                        execute();
                    } catch (Throwable t) {
                        if (t instanceof ThreadStop)
                            running = false;
                        else
                            onException(t);
                    }
                }
            }
        };
    }

    //Starts the queue processing event.
    public void start() {
        running = true;
        run.start();
    }

    //Stops the queue processing event.
    @SuppressWarnings("unchecked")
    public void stop() {
        running = false;
        addEvent((T) new ThreadStopEvent()); //Poison pill event stopper.
    }

    public void join() {
        if (running)
            stop();
        try {
            run.join();
        } catch (InterruptedException e) {
            logger.log("Error when stopping thread", e, LogLevel.ERROR);
        }
    }

    //Adds an object to the processing queue.
    public void addEvent(T event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            logger.log("Error adding to event queue", e, LogLevel.ERROR);
        }
    }

    //pulls an object from the queue, blocks if argument is true.
    protected T getEvent(boolean block) throws ThreadStop {
        T obj;
        if (!block) obj = eventQueue.poll();
        else {
            try {
                obj = eventQueue.take();
            } catch (InterruptedException e) {
                logger.log("Error removing from event queue", e, LogLevel.ERROR);
                throw new ThreadStop();
            }

        }
        if (obj instanceof ThreadStopEvent) {
            running = false;
            throw new ThreadStop();
        }
        return obj;
    }

    //Processing function that is called repeatedly.
    protected abstract void execute() throws ThreadStop;

    protected void onException(Throwable t) {
        logger.log("Exception in thread: " + this, t, LogLevel.ERROR);
    }

    public abstract String toString(); //force downstream to override this.

    public boolean isRunning() {
        return running;
    }

    private static class ThreadStopEvent extends GenericEvent {
        @Override
        public boolean equals(Object o) {
            return o instanceof ThreadStopEvent;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
    protected static class ThreadStop extends Exception {
    }
}
