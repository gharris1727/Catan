package com.gregswebserver.catan;

import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.util.Statics;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static Logger logger;

    public static void main(String[] args) {
        // Load the startup window to create/login to a server.
        logger = new Logger();
        new Statics(); //Just to initialize all of the values.
        Startup startup = new Startup(logger);
        startup.setVisible(true);
    }
}
