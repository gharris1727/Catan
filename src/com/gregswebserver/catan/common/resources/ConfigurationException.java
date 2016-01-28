package com.gregswebserver.catan.common.resources;

/**
 * Created by greg on 1/20/16.
 * Exception thrown when there is invalid data seen when a resource is being loaded from a PropertiesFile
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
