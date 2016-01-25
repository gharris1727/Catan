package com.gregswebserver.catan.client.graphics.masks;

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

    @Override
    public String toString() {
        return "RenderMask(" + width + "/" + height + ")";
    }
}
