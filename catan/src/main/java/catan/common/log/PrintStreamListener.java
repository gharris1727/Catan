package catan.common.log;

import java.io.PrintStream;

/**
 * Created by Greg on 8/12/2014.
 * Log listener that prints log output to the System.out console.
 */
public class PrintStreamListener implements LogListener {

    private final PrintStream output;

    public PrintStreamListener(PrintStream output) {
        this.output = output;
    }

    @Override
    public void onLogEvent(LogEvent e) {
        output.println(e);
    }
}
