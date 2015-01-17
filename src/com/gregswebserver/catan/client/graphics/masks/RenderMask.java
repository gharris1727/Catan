package com.gregswebserver.catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * Mask for rendering complex shapes to the screen.
 * Returns two Iterators describing the shape that should be rendered.
 * Immutable at all times.
 */
public abstract class RenderMask {

    protected int width, height;
    protected int[] padding, widths, cumulative;

    protected void init() {
        if (padding == null || widths == null)
            throw new IllegalStateException("Mask arrays not instantiated.");
        if (height != padding.length || height != widths.length)
            throw new IllegalStateException("Mask height inconsistent");
        cumulative = new int[height];
        int sum = 0;
        for (int i = 0; i < height; i++) {
            sum += padding[i] + widths[i];
            cumulative[i] = sum;
        }
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
        int index = x - padding[y];
        if (y > 0) index += cumulative[y - 1];
        return index;
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

    public int getPixelCount() {
        return cumulative[height - 1];
    }
}
