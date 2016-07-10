package catan.common.event;

import catan.common.log.LogLevel;
import catan.common.log.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Greg on 8/12/2014.
 * Generic for a class that has an event queue on the frontend accepting events, and a event to process and dispatch them.
 * Calls the execute() function repeatedly, but does not necessarily have to block for events.
 * execute() will continue looping until stop() is called and running is set to false.
 */
public abstract class QueuedInputThread<T> {

    public final Logger logger;
    private final BlockingQueue<T> queue;
    private final Thread run;
    private boolean running;

    protected QueuedInputThread(Logger logger) {
        this(logger, new LinkedBlockingQueue<>());
    }

    protected QueuedInputThread(Logger logger, BlockingQueue<T> queue) {
        this.logger = logger;
        this.queue = queue;
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
        run.interrupt();
    }

    public void join() {
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
            queue.put(event);
        } catch (InterruptedException e) {
            logger.log("Error adding to event queue", e, LogLevel.ERROR);
        }
    }

    //pulls an object from the queue, blocks if argument is true.
    protected T getEvent(boolean block) throws ThreadStop {
        if (!block)
            return queue.poll();
        else {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                throw new ThreadStop();
            }
        }
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

    protected static class ThreadStop extends Exception {
    }
}
