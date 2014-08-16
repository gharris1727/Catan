package com.gregswebserver.catan.client.graphics;

/**
 * Created by Greg on 8/14/2014.
 * Helper class to generate iterators describing the masking on a hexagonal sprite.
 * Warning: most of what happens in this class is magic and needs the test class to be sure it works.
 */
public class HexagonalMask extends RenderMask {

    private int height;
    private int width;

    public HexagonalMask(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPadding(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            return (getWidth() - getLineWidth(lineNumber)) / 2;
        }
        return getLeftPadding(getHeight() - 1 - lineNumber);
    }

    public int getLineWidth(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            return width - 2 * (getCenter(lineNumber) / 2);
        }
        return getLineWidth(getHeight() - 1 - lineNumber);
    }

    private int getCenter(int lineNumber) {
        return getHeight() / 2 - lineNumber;
    }
}

