package com.gregswebserver.catan.log;

/**
 * Created by Greg on 8/12/2014.
 * Implemented by a class that should receive log events.
 */
public interface LogListener {

    public void onLogEvent(LogEvent e);
}
