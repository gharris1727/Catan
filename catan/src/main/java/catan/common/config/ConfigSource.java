package catan.common.config;

import catan.common.game.board.hexarray.Coordinate;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by greg on 3/22/16.
 * Class that provides access to configuration details.
 */
public interface ConfigSource extends Iterable<Map.Entry<String, String>> {

    ConfigSource narrow(String prefix);

    String get(String key);

    default int getInt(String key) {
        try {
            String value = get(key);
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read integer from config file key " + key, e);
        }
    }

    default double getDouble(String key) {
        try {
            return Double.parseDouble(get(key));
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read double from config file key " + key, e);
        }
    }

    default Color getColor(String key) {
        try {
            Field field = Class.forName("java.awt.Color").getField(get(key));
            return (Color) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            throw new ConfigurationException("Unable to read color from config file key " + key, e);
        }
    }

    default int getHexColor(String key) {
        try {
            return Integer.parseInt(get(key), 16);
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Unable to read hex color from config file key " + key, e);
        }
    }

    default Point getPoint(String key) {
        return new Point(getInt(key + ".x"), getInt(key + ".y"));
    }

    default Coordinate getCoord(String key) {
        return new Coordinate(getInt(key + ".x"), getInt(key + ".y"));
    }

    default Dimension getDimension(String key) {
        return new Dimension(getInt(key + ".width"), getInt(key + ".height"));
    }

}
