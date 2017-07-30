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
public abstract class QueuedInputThread<T> implements EventProcessor<T> {

    public final Logger logger;
    private final BlockingQueue<T> queue;
    private Thread run;
    private boolean running;

    protected QueuedInputThread(Logger logger) {
        this(logger, new LinkedBlockingQueue<>());
    }

    protected QueuedInputThread(Logger logger, BlockingQueue<T> queue) {
        this.logger = logger;
        this.queue = queue;
    }

    //Starts the queue processing event.
    public void start() {
        if (run != null) {
            throw new IllegalThreadStateException(toString() + " already started.");
        }
        run = new Thread(() -> {
            while (running) {
                try {
                    execute();
                } catch (ThreadStopException ignored) {
                    running = false;
                } catch (Exception t) {
                    onException(t);
                }
            }
        }, toString());
        running = true;
        run.start();
    }

    //Stops the queue processing event.
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
    @Override
    public void addEvent(T event) {
        try {
            queue.put(event);
        } catch (InterruptedException e) {
            logger.log("Error adding to event queue", e, LogLevel.ERROR);
        }
    }

    //pulls an object from the queue, blocks if argument is true.
    protected T getEvent(boolean block) throws ThreadStopException {
        if (block) {
            try {
                return queue.take();
            } catch (InterruptedException ignored) {
                throw new ThreadStopException();
            }
        } else return queue.poll();
    }

    //Processing function that is called repeatedly.
    protected abstract void execute() throws ThreadStopException;

    protected void onException(Throwable t) {
        logger.log("Exception in thread: " + this, t, LogLevel.ERROR);
    }

    public abstract String toString(); //force downstream to override this.

    public boolean isRunning() {
        return running;
    }

    protected static class ThreadStopException extends Exception {
    }
}
