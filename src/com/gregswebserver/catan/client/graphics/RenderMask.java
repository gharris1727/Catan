package com.gregswebserver.catan.client.graphics;

import java.util.ArrayList;

/**
 * Created by Greg on 8/14/2014.
 * Mask for rendering complex shapes to the screen.
 * Returns two Iterators describing the shape that should be rendered.
 */
public abstract class RenderMask {

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract int getLeftPadding(int lineNumber);

    public abstract int getLineWidth(int lineNumber);

    public int getIndex(int x, int y) {
        int index = 0;
        for (int i = 0; i < y; i++) {
            index += getLineWidth(i);
        }
        index += x - getLeftPadding(y);
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
        int index = 0;
        for (int i = 0; i < getHeight(); i++) {
            index += getLineWidth(i);
        }
        return index;
    }
}