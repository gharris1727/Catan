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

    public Logger logger;
    private LinkedBlockingQueue<T> eventQueue;
    private Thread run;
    private boolean running;

    public QueuedInputThread(Logger logger) {
        this.logger = logger;
        eventQueue = new LinkedBlockingQueue<>();
        run = new Thread(toString()) {
            public void run() {
                while (running) {
                    try {
                        execute();
                    } catch (Throwable t) {
                        if (t instanceof ThreadStop)
                            running = false;
                        else
                            logger.log(t, LogLevel.ERROR);
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

    //Adds an object to the processing queue.
    public void addEvent(T event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            logger.log("Link_Error adding to event queue", e, LogLevel.ERROR);
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
                logger.log("Link_Error removing from event queue", e, LogLevel.ERROR);
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

    public abstract String toString(); //force downstream to override this.

    public boolean isRunning() {
        return running;
    }
}
