package com.gregswebserver.catan.client.masks;

import java.util.ArrayList;

/**
 * Created by Greg on 8/14/2014.
 * Mask for rendering complex shapes to the screen.
 * Returns two Iterators describing the shape that should be rendered.
 * Immutable at all times.
 */
public abstract class RenderMask {

    private ArrayList<Integer> cumulativePixels;

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getLeftPadding(int lineNumber);

    public abstract int getLineWidth(int lineNumber);

    public int getIndex(int x, int y) {
        int maxX = getLeftPadding(y) + getLineWidth(y);
        int maxY = getHeight();
        int minX = getLeftPadding(y);
        int minY = 0;
        String msg = "X/" + minX + "/" + x + "/" + maxX + " Y/" + minY + "/" + y + "/" + maxY;
        if (x < minX || y < minY || x > maxX || y > maxY)
            throw new IllegalArgumentException(msg);
        getPixelCount();
        int index = x - getLeftPadding(y);
        if (y > 0) index += cumulativePixels.get(y - 1);
        return index;
    }

    public ArrayList<Integer> getLeftPadding() {
        ArrayList<Integer> padding = new ArrayList<>(getHeight());
        for (int i = 0; i < getHeight(); i++) {
            padding.add(getLeftPadding(i));
        }
        return padding;
    }

    public ArrayList<Integer> getLineWidth() {
        ArrayList<Integer> width = new ArrayList<>(getHeight());
        for (int i = 0; i < getHeight(); i++) {
            width.add(getLineWidth(i));
        }
        return width;
    }

    public int getPixelCount() {
        if (cumulativePixels == null) {
            cumulativePixels = new ArrayList<>(getHeight());
            int sum = 0;
            for (int i = 0; i < getHeight(); i++) {
                sum += getLineWidth(i);
                cumulativePixels.add(sum);
            }
        }
        return cumulativePixels.get(cumulativePixels.size() - 1);
    }
}