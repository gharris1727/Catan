package com.gregswebserver.catan.log;

import com.gregswebserver.catan.event.GenericEvent;

/**
 * Created by Greg on 8/12/2014.
 * LogEvent sent to the LogListeners.
 */
public class LogEvent extends GenericEvent {

    private String text;

    public LogEvent(String s, LogLevel l) {
        text = "[" + l.name() + "] " + s;
    }

    public String toString() {
        return text;
    }
}
