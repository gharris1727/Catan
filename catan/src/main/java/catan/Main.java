package catan;

import catan.common.launcher.GraphicalLauncher;
import catan.common.launcher.HeadlessLauncher;
import catan.common.launcher.StartupOptions;

import java.awt.GraphicsEnvironment;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

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
            Logger logger = Logger.getLogger("catan.Main");
            logger.addHandler(new ConsoleHandler());
            // Choose the relevant launcher.
            if (GraphicsEnvironment.isHeadless()) {
                new HeadlessLauncher(options).waitForClose();
            } else {
                new GraphicalLauncher(options).waitForClose();
            }
        }
    }
}
