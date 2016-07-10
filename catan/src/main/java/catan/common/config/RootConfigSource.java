package catan.common.config;

import catan.common.game.board.hexarray.Coordinate;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * Created by greg on 3/26/16.
 * A ConfigSource that is responsible for actually storing config details.
 */
public abstract class RootConfigSource implements ConfigSource {

    @Override
    public String get(String key) {
        String found = search(key);
        if (found == null)
            throw new ConfigurationException("Unable to read value from config file key " + key);
        return found;
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
            target = getEntry(partial);
        } else {
            target = getEntry(key);
        }
        if (target == null)
            return null;
        String recurse = search(target);
        return recurse == null ? target : recurse;
    }

    private String tryRedirect(String key) {
        String redirect = getEntry(key);
        return (redirect == null) ? key : redirect;
    }

    @Override
    public ConfigSource narrow(String prefix) {
        return new NarrowedConfigSource(prefix);
    }

    private class NarrowedConfigSource implements ConfigSource {
        private final String prefix;

        private NarrowedConfigSource(String root) {
            this.prefix = root + ".";
        }

        @Override
        public ConfigSource narrow(String prefix) {
            return new NarrowedConfigSource(this.prefix + prefix);
        }

        @Override
        public String get(String key) {
            return RootConfigSource.this.get(prefix + key);
        }

        @Override
        public int getInt(String key) {
            return RootConfigSource.this.getInt(prefix + key);
        }

        @Override
        public double getDouble(String key) {
            return RootConfigSource.this.getDouble(prefix + key);
        }

        @Override
        public Color getColor(String key) {
            return RootConfigSource.this.getColor(prefix + key);
        }

        @Override
        public int getHexColor(String key) {
            return RootConfigSource.this.getHexColor(prefix + key);
        }

        @Override
        public Point getPoint(String key) {
            return RootConfigSource.this.getPoint(prefix + key);
        }

        @Override
        public Coordinate getCoord(String key) {
            return RootConfigSource.this.getCoord(prefix + key);
        }

        @Override
        public Dimension getDimension(String key) {
            return RootConfigSource.this.getDimension(prefix + key);
        }
    }

    protected abstract String getEntry(String key);
}
