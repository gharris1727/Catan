package com.gregswebserver.catan.common.resources;

/**
 * Created by Greg on 1/6/2015.
 * Exception encountered when loading some resource.
 */
public class ResourceLoadException extends Exception {

    public ResourceLoadException(String name) {
        super("Problem loading resource " + name);
    }

    public ResourceLoadException(Throwable cause) {
        super("Problem loading resource.", cause);
    }
}
