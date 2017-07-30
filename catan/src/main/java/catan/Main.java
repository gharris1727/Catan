package catan;

import catan.common.launcher.GraphicalLauncher;
import catan.common.launcher.HeadlessLauncher;
import catan.common.launcher.StartupOptions;
import catan.common.log.Logger;

import java.awt.*;

/**
 * Created by Greg on 8/8/2014.
 * catan.Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public final class Main {

    private Main() {
    }

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
