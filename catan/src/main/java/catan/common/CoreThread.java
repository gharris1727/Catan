package catan.common;

import catan.common.event.ExternalEvent;
import catan.common.event.GenericEvent;
import catan.common.event.InternalEvent;
import catan.common.event.QueuedInputThread;
import catan.common.network.NetEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by greg on 1/7/16.
 * Superclass of the Server and Client, and contains some code common in each.
 * Handles dispatching events by looking at their types.
 */
public abstract class CoreThread extends QueuedInputThread<GenericEvent> {

    private final Logger logger = Logger.getLogger(getClass().getName());

    @Override
    protected void execute() throws ThreadStopException {
        GenericEvent event = getEvent(true);
        if (event instanceof ExternalEvent) {
            externalEvent((ExternalEvent) event);
        } else if (event instanceof InternalEvent) {
            internalEvent((InternalEvent) event);
        } else if (event instanceof NetEvent) {
            netEvent((NetEvent) event);
        } else {
            logger.log(Level.SEVERE, "Received invalid GenericEvent.");
        }

    }

    protected abstract void externalEvent(ExternalEvent event);

    protected abstract void internalEvent(InternalEvent event);

    protected abstract void netEvent(NetEvent event);

}
