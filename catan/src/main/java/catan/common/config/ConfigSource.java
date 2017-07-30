package catan.common.config;

import catan.client.graphics.masks.*;
import catan.common.game.board.hexarray.Coordinate;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Map.Entry;

/**
 * Created by greg on 3/22/16.
 * Class that provides access to configuration details.
 */
public interface ConfigSource extends Iterable<Entry<String, String>> {

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

    default RenderMask getRenderMask(String key) {
        ConfigSource config = narrow(key);
        Dimension size = null;
        ConfigurationException sizeExcept = null;
        try {
            size = config.getDimension("size");
        } catch (ConfigurationException e) {
            sizeExcept = e;
        }
        switch(config.get("type")) {
            case "angle":
                if (sizeExcept != null)
                    throw sizeExcept;
                try {
                    Dimension offset = config.getDimension("offset");
                    return new AngleRectangularMask(size, offset);
                } catch (ConfigurationException ignored) {
                    return new AngleRectangularMask(size);
                }
            case "chevron":
                if (sizeExcept != null)
                    throw sizeExcept;
                int centerOffset = config.getInt("offset");
                return new ChevronMask(size, centerOffset);
            case "flipped":
                RenderMask flipOther = config.getRenderMask("mask");
                FlippedMask.Direction flipDirection;
                switch (config.get("direction").toLowerCase()) {
                    case "0":
                    case "v":
                    case "vert":
                    case "vertical":
                    case "up":
                    case "down":
                        flipDirection = FlippedMask.Direction.VERTICAL;
                        break;
                    case "1":
                    case "h":
                    case "horiz":
                    case "horizontal":
                    case "left":
                    case "right":
                        flipDirection = FlippedMask.Direction.HORIZONTAL;
                        break;
                    default:
                        throw new ConfigurationException("Unknown FlippedMask direction: " + config.get("direction"));
                }
                return new FlippedMask(flipOther, flipDirection);
            case "hexagonal":
                if (sizeExcept != null)
                    throw sizeExcept;
                return new HexagonalMask(size);
            case "inverted":
                RenderMask invertOther = config.getRenderMask("mask");
                InvertedMask.Direction invertDirection;
                switch (config.get("direction").toLowerCase()) {
                    case "0":
                    case "l":
                    case "left":
                        invertDirection = InvertedMask.Direction.LEFT;
                        break;
                    case "1":
                    case "r":
                    case "right":
                        invertDirection = InvertedMask.Direction.RIGHT;
                        break;
                    default:
                        throw new ConfigurationException("Unknown InvertedMask direction: " + config.get("direction"));
                }
                return new InvertedMask(invertOther, invertDirection);
            case "offset":
                RenderMask offsetOther = config.getRenderMask("mask");
                Point offset = config.getPoint("offset");
                return new OffsetMask(offsetOther, offset);
            case "rectangular":
                if (sizeExcept != null)
                    throw sizeExcept;
                return new RectangularMask(size);
            case "rounded":
                if (sizeExcept != null)
                    throw sizeExcept;
                try {
                    Dimension corner = config.getDimension("corner");
                    return new RoundedRectangularMask(size, corner);
                } catch (ConfigurationException ignored) {
                    return new RoundedRectangularMask(size);
                }
            case "round":
                if (sizeExcept != null)
                    throw sizeExcept;
                return new RoundMask(size);
            case "triangular":
                if (sizeExcept != null)
                    throw sizeExcept;
                return new TriangularMask(size);
            default:
                throw new ConfigurationException("Unknown mask type");
        }
    }
}
