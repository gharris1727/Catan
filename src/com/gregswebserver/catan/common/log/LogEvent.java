package com.gregswebserver.catan.common.log;

/**
 * Created by Greg on 8/12/2014.
 * LogEvent sent to the LogListeners.
 */
public class LogEvent {

    private final String text;

    public LogEvent(String s, LogLevel l) {
        text = "[" + l.name() + "] " + s;
    }

    public String toString() {
        return text;
    }
}
