package com.gregswebserver.catan.common.log;

/**
 * Created by Greg on 8/12/2014.
 * Implemented by a class that should process log events.
 */
public interface LogListener {

    public void onLogEvent(LogEvent e);
}
