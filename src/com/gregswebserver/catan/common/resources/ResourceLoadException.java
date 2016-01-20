package com.gregswebserver.catan.common.resources;

/**
 * Created by Greg on 1/6/2015.
 * Exception encountered when loading some resource.
 */
public class ResourceLoadException extends RuntimeException {

    public ResourceLoadException(String message) {
        super("Problem loading resource: " + message);
    }

    public ResourceLoadException(String message, Throwable cause) {
        super("Problem loading resource: " + message, cause);
    }
}
