package com.gregswebserver.catan;

import com.gregswebserver.catan.common.Startup;
import com.gregswebserver.catan.common.log.LogLevel;
import com.gregswebserver.catan.common.log.Logger;
import com.gregswebserver.catan.common.resources.ConfigFile;
import com.gregswebserver.catan.common.resources.ResourceLoader;

import java.io.IOException;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static Logger logger;
    public static ConfigFile staticConfig;

    public static void main(String[] args) {
        // Create a logger to begin storing log information
        logger = new Logger();
        // Create the static root configuration
        staticConfig = new ConfigFile(logger, "config/static.properties", "Static root configuration");
        // Attempt to load the static configuration from file
        try {
            staticConfig.open();
        } catch (IOException e) {
            logger.log(e, LogLevel.WARN);
        }
        new ResourceLoader();
        Startup startup = new Startup(logger);
        startup.setVisible(true);
    }

    public static void quit() {
        try {
            staticConfig.close();
        } catch (IOException e) {
            logger.log(e,LogLevel.WARN);
        }
    }
}
