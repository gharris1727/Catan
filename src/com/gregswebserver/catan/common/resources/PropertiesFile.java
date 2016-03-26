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
 * Class to access configuration details
 */
public class PropertiesFile implements Iterable<Map.Entry<String, String>>, ConfigSource {

    private final String path;
    private final String comment;
    private final Properties config;
    private boolean needsSaving;
    private Source source;

    public PropertiesFile(String path, String comment) {
        this.path = path;
        this.comment = comment;
        config = new Properties();
        needsSaving = false;
        source = Source.UNLOADED;
        try {
            config.load(new BufferedReader(new FileReader(
                    ExternalResource.getCurrentDirectory() +
                            ExternalResource.getConfigDirectory() +
                            path)));
            source = Source.CURRENTDIR;
        } catch (IOException ignored) {
        }
        if (source == Source.UNLOADED)
            try {
                config.load(new BufferedReader(new FileReader(
                        ExternalResource.getUserDataDirectory() +
                                ExternalResource.getConfigDirectory() +
                                path)));
                source = Source.USERDIR;
            } catch (IOException ignored) {
            }
        if (source == Source.UNLOADED)
            try {
                config.load(ExternalResource.class.getResourceAsStream(
                        ExternalResource.getResourceDataDirectory() +
                                ExternalResource.getConfigDirectory() +
                                path));
                source = Source.DEFAULT;
            } catch (IOException ignored) {
            }

    }

    public void close() throws IOException {
        if (needsSaving) {
            File file;
            switch (source) {
                case CURRENTDIR:
                    file = new File(ExternalResource.getCurrentDirectory() + path);
                    break;
                default:
                    file = new File(ExternalResource.getUserDataDirectory() + path);
                    break;
            }
            if (!file.exists() && file.getParentFile().mkdirs()) {
                if (!file.createNewFile())
                    throw new IOException("Unable to create new file to save.");
            }
            config.store(new BufferedWriter(new FileWriter(file)), comment);
        }
    }

    @Override
    public ConfigSource narrow(String prefix) {
        return new PropertiesFileNode(prefix);
    }

    @Override
    public String get(String key) {
        String found = search(key);
        if (found == null)
            throw new ConfigurationException("Unable to read value from config file key " + key);
        return found;
    }

    private String search(String key) {
        //Bottom out on null/empty strings
        if (key == null || key.length() == 0)
            throw new ConfigurationException("Unable to read null key");
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
        String recurse = search(target);
        return recurse == null ? target : recurse;
    }

    private String tryRedirect(String key) {
        String redirect = config.getProperty(key);
        return (redirect == null) ? key : redirect;
    }

    @Override
    public int getInt(String key) {
        try {
            String value = get(key);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read integer from config file key " + key, e);
        }
    }

    @Override
    public double getDouble(String key) {
        try {
            return Double.parseDouble(get(key));
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read double from config file key " + key, e);
        }
    }

    @Override
    public Color getColor(String key) {
        try {
            Field field = Class.forName("java.awt.Color").getField(get(key));
            return (Color) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            throw new ConfigurationException("Unable to read color from config file key " + key, e);
        }
    }

    @Override
    public int getHexColor(String key) {
        try {
            return Integer.parseInt(get(key), 16);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read hex color from config file key " + key, e);
        }
    }

    @Override
    public Point getPoint(String key) {
        return new Point(getInt(key + ".x"), getInt(key + ".y"));
    }

    @Override
    public Coordinate getCoord(String key) {
        return new Coordinate(getInt(key + ".x"), getInt(key + ".y"));
    }

    @Override
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

    private enum Source {
        UNLOADED, CURRENTDIR, USERDIR, DEFAULT
    }

    public class PropertiesFileNode implements ConfigSource {
        private final String prefix;

        public PropertiesFileNode(String root) {
            this.prefix = root + ".";
        }

        @Override
        public ConfigSource narrow(String prefix) {
            return new PropertiesFileNode(this.prefix + prefix);
        }

        @Override
        public String get(String key) {
            return PropertiesFile.this.get(prefix + key);
        }

        @Override
        public int getInt(String key) {
            return PropertiesFile.this.getInt(prefix + key);
        }

        @Override
        public double getDouble(String key) {
            return PropertiesFile.this.getDouble(prefix + key);
        }

        @Override
        public Color getColor(String key) {
            return PropertiesFile.this.getColor(prefix + key);
        }

        @Override
        public int getHexColor(String key) {
            return PropertiesFile.this.getHexColor(prefix + key);
        }

        @Override
        public Point getPoint(String key) {
            return PropertiesFile.this.getPoint(prefix + key);
        }

        @Override
        public Coordinate getCoord(String key) {
            return PropertiesFile.this.getCoord(prefix + key);
        }

        @Override
        public Dimension getDimension(String key) {
            return PropertiesFile.this.getDimension(prefix + key);
        }
    }
}
