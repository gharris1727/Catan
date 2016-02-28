package com.gregswebserver.catan.common.resources;

import com.gregswebserver.catan.ExternalResource;
import com.gregswebserver.catan.common.game.board.hexarray.Coordinate;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Created by greg on 1/18/16.
 * Class to access the static.properties configuration details
 */
public class PropertiesFile implements Iterable<Map.Entry<String, String>> {

    private final String path;
    private final String comment;
    private final Properties config;
    private boolean needsSaving;

    public PropertiesFile(String path, String comment) throws IOException {
        this.path = path;
        this.comment = comment;
        config = new Properties();
        needsSaving = false;
        try {
            config.load(new BufferedReader(new FileReader(ExternalResource.getUserDataDirectory() + path)));
        } catch (IOException e) {
            config.load(ExternalResource.class.getResourceAsStream(ExternalResource.getResourceDataDirectory() + path));
        }
    }

    public void close() throws IOException {
        File file = new File(ExternalResource.getUserDataDirectory() + path);
        if (needsSaving) {
            if (!file.exists() && file.getParentFile().mkdirs()) {
                if (!file.createNewFile())
                    throw new IOException("Unable to create new file to save.");
            }
            config.store(new BufferedWriter(new FileWriter(file)), comment);
        }
    }

    public String get(String key) {
        //Bottom out on null/empty strings
        if (key == null || key.length() == 0)
            return null;
        //Split the key by the periods in its length
        String[] keyParts = key.split("\\.");
        //Find the key we should be looking for
        String target;
        //If there is more than one part, check the linear redirects.
        if (keyParts.length > 1) {
            String partial = tryRedirect(keyParts[0]);
            for (int i = 1; i < keyParts.length - 1; i++)
                partial = tryRedirect(partial + "." + keyParts[i]);
            partial += "." + keyParts[keyParts.length - 1];
            target = config.getProperty(partial);
        } else {
            target = config.getProperty(key);
        }
        if (target == null)
            return null;
        String recurse = get(target);
        return recurse == null ? target : recurse;
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

    public Coordinate getCoord(String key) {
        return new Coordinate(getInt(key + ".x"), getInt(key + ".y"));
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

    public void clear() {
        config.clear();
    }

    @Override
    public Iterator<Map.Entry<String, String>> iterator() {
        //Get the iterator from the internal storage
        Iterator<Map.Entry<Object, Object>> objIterator = config.entrySet().iterator();
        //We cant cast, so we need to cast everything individually.
        return new Iterator<Map.Entry<String, String>>() {
            @Override
            public boolean hasNext() {
                return objIterator.hasNext();
            }

            @Override
            public Map.Entry<String, String> next() {
                Map.Entry<Object, Object> objEntry = objIterator.next();
                return new Map.Entry<String, String>() {
                    @Override
                    public String getKey() {
                        return (String) objEntry.getKey();
                    }

                    @Override
                    public String getValue() {
                        return (String) objEntry.getValue();
                    }

                    @Override
                    public String setValue(String s) {
                        return (String) objEntry.setValue(s);
                    }
                };
            }
        };
    }
}
