package catan.common;

import catan.common.event.ExternalEvent;
import catan.common.event.GenericEvent;
import catan.common.event.InternalEvent;
import catan.common.event.QueuedInputThread;
import catan.common.log.LogLevel;
import catan.common.log.Logger;
import catan.common.network.NetEvent;

/**
 * Created by greg on 1/7/16.
 * Superclass of the Server and Client, and contains some code common in each.
 * Handles dispatching events by looking at their types.
 */
public abstract class CoreThread extends QueuedInputThread<GenericEvent> {

    protected CoreThread(Logger logger) {
        super(logger);
    }

    @Override
    protected void execute() throws ThreadStopException {
        GenericEvent event = getEvent(true);
        //logger.log(this + " Received " + event, LogLevel.DEBUG);
        if (event instanceof ExternalEvent) {
            externalEvent((ExternalEvent) event);
        } else if (event instanceof InternalEvent) {
            internalEvent((InternalEvent) event);
        } else if (event instanceof NetEvent) {
            netEvent((NetEvent) event);
        } else {
            logger.log("Received invalid GenericEvent.", LogLevel.WARN);
        }

    }

    protected abstract void externalEvent(ExternalEvent event);

    protected abstract void internalEvent(InternalEvent event);

    protected abstract void netEvent(NetEvent event);

}
