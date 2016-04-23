package com.gregswebserver.catan;

import com.gregswebserver.catan.common.Launcher;
import com.gregswebserver.catan.common.log.Logger;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static void main(String[] args) {
        // Create a logger to begin storing log information
        Logger logger = new Logger();
        logger.useStdOut();
        // Initialize the launcher window
        //TODO: add a command-line launcher to allow for headless servers.
        Launcher launcher = new Launcher(logger);
        launcher.setVisible(true);
    }
}
