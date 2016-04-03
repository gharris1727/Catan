package com.gregswebserver.catan.client.graphics.masks;

import com.gregswebserver.catan.common.IllegalStateException;
import com.gregswebserver.catan.common.config.ConfigSource;
import com.gregswebserver.catan.common.config.ConfigurationException;

import java.awt.*;
import java.util.Arrays;

/**
 * Created by Greg on 8/14/2014.
 * Mask for rendering complex shapes to the screen.
 * Returns two Iterators describing the shape that should be rendered.
 * Immutable at all times.
 */
public abstract class RenderMask {

    protected int width, height;
    protected int[] padding;
    protected int[] widths;

    protected void init() {
        if (padding == null || widths == null)
            throw new IllegalStateException("Mask arrays not instantiated.");
        if (height != padding.length || height != widths.length)
            throw new IllegalStateException("Mask height inconsistent");
        for (int row = 0; row < height; row++)
            if (padding[row] + widths[row] > width)
                throw new IllegalStateException("Mask width inconsistent.");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public int[] getPadding() {
        return padding;
    }

    public int[] getWidths() {
        return widths;
    }

    public int getIndex(Point p) {
        return getIndex(p.x, p.y);
    }

    public int getIndex(int x, int y) {
        if (!containsPoint(x, y))
            throw new IllegalArgumentException("Coordinate outside of mask bounds.");
        return y*getWidth() + x;
    }

    public boolean hasContent() {
        return width > 0 && height > 0;
    }

    public boolean containsPoint(Point p) {
        return containsPoint(p.x, p.y);
    }

    public boolean containsPoint(int x, int y) {
        if (y < 0 || y >= height)
            return false;
        int minX = padding[y];
        int maxX = minX + widths[y];
        return x >= minX && x < maxX;
    }

    @Override
    public int hashCode() {
        int hash = width;
        hash = 31 * hash + height;
        hash = 31 * hash + Arrays.hashCode(padding);
        hash = 31 * hash + Arrays.hashCode(widths);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof RenderMask) {
            RenderMask mask = (RenderMask) o;
            return width == mask.width && height == mask.height
                    && Arrays.equals(padding, mask.padding)
                    && Arrays.equals(widths, mask.widths);
        }
        return false;
    }

    public static RenderMask parseMask(ConfigSource config) {
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
                RenderMask flipOther = parseMask(config.narrow("mask"));
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
                RenderMask invertOther = parseMask(config.narrow("mask"));
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
                RenderMask offsetOther = parseMask(config.narrow("mask"));
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

    @Override
    public String toString() {
        return "RenderMask(" + width + "/" + height + ")";
    }
}
