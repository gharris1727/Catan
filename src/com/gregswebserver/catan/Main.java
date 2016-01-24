package com.gregswebserver.catan;

import com.gregswebserver.catan.common.Startup;
import com.gregswebserver.catan.common.log.Logger;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static Logger logger;

    public static void main(String[] args) {
        // Create a logger to begin storing log information
        logger = new Logger();
        logger.useStdOut();
        // Initialize the startup window
        Startup startup = new Startup(logger);
        startup.setVisible(true);
    }
}
