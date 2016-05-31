package com.gregswebserver.catan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by greg on 1/19/16.
 * Directory manager for external resources
 */
public class ExternalResource {

    public static String getConfigDirectory() {
        return "config/";
    }

    public static String getGraphicsDirectory() {
        return "graphics/";
    }

    public static InputStream getStaticResource(String s) throws FileNotFoundException {
        InputStream stream = ExternalResource.class.getResourceAsStream("/" + s);
        if (stream == null)
            throw new FileNotFoundException("Unable to load: " + s);
        return stream;
    }

    public static File getUserResource(String s) {
        return new File(System.getProperty("user.home") + "/.catan/" + s);
    }
}
