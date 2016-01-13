package com.gregswebserver.catan.common.log;

/**
 * Created by Greg on 8/12/2014.
 * Log listener that prints log output to the System.out console.
 */
public class StdOutListener implements LogListener {
    @Override
    public void onLogEvent(LogEvent e) {
        System.out.println(e.toString());
    }
}
