package com.gregswebserver.catan.event;

import com.gregswebserver.catan.log.LogLevel;
import com.gregswebserver.catan.log.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Greg on 8/12/2014.
 * Generic for a class that has an event queue on the frontend accepting GenericEvents, and a thread to process and dispatch them.
 */
public abstract class EventQueueThread<T> {

    public Logger logger;
    protected LinkedBlockingQueue<T> eventQueue;
    protected Thread run;
    protected boolean running;

    public EventQueueThread(Logger logger) {
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

    public void start() {
        run.start();
        running = true;
    }

    public void stop() {
        running = false;
    }

    public void addEvent(T event) {
        try {
            eventQueue.put(event);
        } catch (InterruptedException e) {
            logger.log("Error adding to event queue", e, LogLevel.ERROR);
        }
    }

    protected T getEvent(boolean block) {
        if (!block) return eventQueue.poll();
        try {
            return eventQueue.take();
        } catch (InterruptedException e) {
            logger.log("Error removing from event queue", e, LogLevel.ERROR);
            return null;
        }
    }

    protected abstract void execute();

}
