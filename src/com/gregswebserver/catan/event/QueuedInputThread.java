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
public abstract class QueuedInputThread<T> {

    public Logger logger;
    protected LinkedBlockingQueue<T> eventQueue;
    protected Thread run;
    protected boolean running;

    public QueuedInputThread(Logger logger) {
        this.logger = logger;
        eventQueue = new LinkedBlockingQueue<>();
        run = new Thread(this.getClass().getName()) {
            public void run() {
                while (running) {
                    execute();
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
    protected T getEvent(boolean block) {
        if (!block) return eventQueue.poll();
        try {
            return eventQueue.take();
        } catch (InterruptedException e) {
            logger.log("Error removing from event queue", e, LogLevel.ERROR);
            return null;
        }
    }

    //Processing function that is called repeatedly.
    protected abstract void execute();

}
