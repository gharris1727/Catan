package com.gregswebserver.catan.event;

import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Greg on 8/12/2014.
 * Generic for a class that has an event queue on the frontend accepting events, and a thread to process and dispatch them.
 * Calls the execute() function repeatedly, but does not necessarily have to block for events.
 * execute() will continue looping until stop() is called and running is set to false.
 */
public abstract class QueuedInputThread {

    public Logger logger;
    private LinkedBlockingQueue<GenericEvent> eventQueue;
    private Thread run;
    private boolean running;

    public QueuedInputThread(Logger logger) {
        this.logger = logger;
        eventQueue = new LinkedBlockingQueue<>();
        run = new Thread(this.getClass().getName()) {
            public void run() {
                while (running) {
                    try {
                        execute();
                    } catch (ThreadStop threadStop) {
                        running = false;
                    }
                }
            }
        };
    }

    //Starts the queue processing thread.
    public void start() {
        running = true;
        run.start();
    }

    //Stops the queue processing thread.
    public void stop() {
        running = false;
        addEvent(new ThreadStopEvent()); //Poison pill thread stopper.
    }

    //Adds an object to the processing queue.
    public void addEvent(GenericEvent event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            logger.log("Error adding to event queue", e, LogLevel.ERROR);
        }
    }

    //pulls an object from the queue, blocks if argument is true.
    protected GenericEvent getEvent(boolean block) throws ThreadStop {
        GenericEvent obj;
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

    public abstract String toString(); //force downstream to override this.

}
