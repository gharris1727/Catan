package com.gregswebserver.catan;

import java.io.File;

/**
 * Created by greg on 1/19/16.
 * Directory manager for external resources
 */
public class ExternalResource {

    public static String getCurrentDirectory() {
        return System.getProperty("user.dir");
    }

    public static String getUserDataDirectory() {
        return System.getProperty("user.home") + File.separator + ".catan" + File.separator;
    }

    public static String getResourceDataDirectory() {
        return File.separator;
    }

    public static String getConfigDirectory() {
        return "config" + File.separator;
    }
}
