package com.gregswebserver.catan;

import com.gregswebserver.catan.common.launcher.GraphicalLauncher;
import com.gregswebserver.catan.common.launcher.HeadlessLauncher;
import com.gregswebserver.catan.common.launcher.StartupOptions;
import com.gregswebserver.catan.common.log.Logger;

import java.awt.*;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static void main(String[] args) {
        // Interpret the command line arguments.
        StartupOptions options = new StartupOptions(args);
        if (options.hasError()) {
            options.printError();
        } else {
            // Create a logger to begin storing log information
            Logger logger = new Logger();
            logger.useStdOut();
            // Choose the relevant launcher.
            if (GraphicsEnvironment.isHeadless()) {
                new HeadlessLauncher(options, logger).waitForClose();
            } else {
                new GraphicalLauncher(options, logger).waitForClose();
            }
        }
    }
}
