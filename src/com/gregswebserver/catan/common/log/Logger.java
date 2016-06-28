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

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    private final List<LogListener> listeners;

    public Logger() {
        listeners = new LinkedList<>();
    }

    public void useStdOut() {
        addListener(new PrintStreamListener(System.out));
    }

    public void useStdErr() {
        addListener(new PrintStreamListener(System.err));
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
