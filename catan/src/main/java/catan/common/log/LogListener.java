package catan.common.log;

/**
 * Created by Greg on 8/12/2014.
 * Implemented by a class that should process log events.
 */
public interface LogListener {

    void onLogEvent(LogEvent e);
}
