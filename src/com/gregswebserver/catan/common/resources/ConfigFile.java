package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.ExternalResource;
import com.gregswebserver.catan.common.log.Logger;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by greg on 1/18/16.
 * Class to access the static.properties configuration details
 */
public class ConfigFile {

    private final Logger logger;
    private final String path;
    private final String comment;
    private final Properties config;
    private boolean needsSaving;

    public ConfigFile(Logger logger, String path, String comment) {
        this.logger = logger;
        this.path = path;
        this.comment = comment;
        config = new Properties();
        needsSaving = false;
    }

    public void open() throws IOException {
        try {
            config.load(new BufferedReader(new FileReader(ExternalResource.getUserDataDirectory() + path)));
        } catch (IOException e) {
            config.load(ExternalResource.class.getResourceAsStream(ExternalResource.getResourceDataDirectory() + path));
        }
    }

    public void close() throws IOException {
        File file = new File(ExternalResource.getUserDataDirectory() + path);
        if (needsSaving) {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            config.store(new BufferedWriter(new FileWriter(file)), comment);
        }
    }

    public String get(String key) {
        // Continue only if the key provided was not null.
        if (key != null) {
            // Break the key up by delimiters for redirects
            String[] keyParts = key.split("\\.");
            // Start looking for the root token.
            String newKey = tryRedirect(keyParts[0]);
            for(int i = 1; i < keyParts.length - 1; i++)
                newKey = tryRedirect(newKey + "." + keyParts[i]);
            if (keyParts.length > 1)
                newKey += "." + keyParts[keyParts.length-1];
            String found = config.getProperty(newKey);
            if (found == null)
                return null;
            String recurse = get(found);
            return (recurse == null) ? found : recurse;

        }
        return null;
    }

    private String tryRedirect(String key) {
        String redirect = config.getProperty(key);
        return (redirect == null) ? key : redirect;
    }

    public int getInt(String key) {
        try {
            String value = get(key);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read integer from config file key " + key, e);
        }
    }

    public double getDouble(String key) {
        try {
            return Double.parseDouble(get(key));
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read double from config file key " + key, e);
        }
    }

    public Color getColor(String key) {
        try {
            Field field = Class.forName("java.awt.Color").getField(get(key));
            return (Color) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            throw new ConfigurationException("Unable to read color from config file key " + key, e);
        }
    }

    public Point getPoint(String key) {
        return new Point(getInt(key + ".x"), getInt(key + ".y"));
    }

    public Dimension getDimension(String key) {
        return new Dimension(getInt(key + ".width"), getInt(key + ".height"));
    }

    public void set(String key, String value) {
        if (key != null && value != null) {
            config.setProperty(key, value);
            needsSaving = true;
        }
    }
}
