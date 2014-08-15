package com.gregswebserver.catan;

import com.gregswebserver.catan.log.Logger;
import com.gregswebserver.catan.userinterface.Startup;

import java.awt.*;

/**
 * Created by Greg on 8/8/2014.
 * Main game file that contains the main(String[] args) to execute the program from an executable jar.
 */
public class Main {

    public static Logger logger;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // Load the startup window to create/login to a server.
            logger = new Logger();
            Startup startup = new Startup(logger);
            startup.setVisible(true);
        });
    }
}
