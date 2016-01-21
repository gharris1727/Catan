package com.gregswebserver.catan.common.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Greg on 8/11/2014.
 * Logging class to handle errors and info messages.
 */
public class Logger {

    private final List<LogListener> listeners;

    public Logger() {
        listeners = new LinkedList<>();
        addListener(new StdOutListener());
        //TODO: remove when deployed, prints to IDE console.
    }

    public void addListener(LogListener listener) {
        listeners.add(listener);
    }

    public void removeListener(LogListener listener) {
        listeners.remove(listener);
    }

    public void log(String s, Throwable t, LogLevel l) {
        fire(new LogEvent(s + "\n" + printStack(t), l));
    }

    public void log(Throwable t, LogLevel l) {
        fire(new LogEvent(printStack(t), l));
    }

    public void log(String s, LogLevel l) {
        fire(new LogEvent(s, l));
    }

    public void debug(Object origin, String s) {
        fire(new LogEvent("<" + origin + "> " + s, LogLevel.DEBUG));
    }

    private String printStack(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private void fire(LogEvent e) {
        for (LogListener l : listeners) {
            l.onLogEvent(e);
        }
    }
}
