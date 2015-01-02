package com.gregswebserver.catan.client.graphics.masks;

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

    public int[] getPadding() {
        return padding;
    }

    public int[] getWidths() {
        return widths;
    }

    public int getIndex(int x, int y) {
        if (y < 0 || y >= height)
            throw new IllegalArgumentException("Y Coordinate out of bounds.");
        int minX = padding[y];
        int maxX = minX + widths[y];
        if (x < minX || x >= maxX)
            throw new IllegalArgumentException("X Coordinate out of bounds.");
        int index = x - padding[y];
        if (y > 0) index += cumulative[y - 1];
        return index;
    }

    public int getPixelCount() {
        return cumulative[height - 1];
    }
}
