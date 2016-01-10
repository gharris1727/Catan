package com.gregswebserver.catan.common;

import com.gregswebserver.catan.common.crypto.AuthToken;
import com.gregswebserver.catan.common.event.*;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;

/**
 * Created by greg on 1/7/16.
 * Superclass of the Server and Client, and contains some code common in each.
 * Handles dispatching events by looking at their types.
 */
public abstract class CoreThread extends QueuedInputThread<GenericEvent> {

    protected AuthToken token;

    public CoreThread(Logger logger) {
        super(logger);
    }

    protected void execute() throws ThreadStop {
        GenericEvent event = getEvent(true);
        logger.log(this + " Received " + event, LogLevel.DEBUG);
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

    public AuthToken getToken() {
        return token;
    }
}
