package catan.client.graphics.masks;

import catan.common.IllegalStateException;

import java.util.Arrays;

/**
 * Created by Greg on 8/14/2014.
 * Mask for rendering complex shapes to the screen.
 * Returns two Iterators describing the shape that should be rendered.
 * Immutable at all times.
 */
public class CachedMask implements RenderMask {

    private final int width;
    private final int height;
    private final int[] padding;
    private final int[] widths;

    protected CachedMask(RenderMask other) {
        width = other.getWidth();
        height = other.getHeight();
        padding = new int[height];
        widths = new int[height];
        for (int i = 0; i < height; i++) {
            padding[i] = other.getLinePadding(i);
            widths[i] = other.getLineWidth(i);
            if ((padding[i] + widths[i]) > width)
                throw new IllegalStateException("Mask width inconsistent.");
        }
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getLinePadding(int line) {
        return padding[line];
    }

    @Override
    public int getLineWidth(int line) {
        return widths[line];
    }

    @Override
    public int hashCode() {
        int hash = width;
        hash = (31 * hash) + height;
        hash = (31 * hash) + Arrays.hashCode(padding);
        hash = (31 * hash) + Arrays.hashCode(widths);
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o instanceof CachedMask) {
            CachedMask mask = (CachedMask) o;
            return (width == mask.width) && (height == mask.height)
                    && Arrays.equals(padding, mask.padding)
                    && Arrays.equals(widths, mask.widths);
        }
        return false;
    }

    @Override
    public String toString() {
        return "CachedMask(" + width + "/" + height + ")";
    }
}
