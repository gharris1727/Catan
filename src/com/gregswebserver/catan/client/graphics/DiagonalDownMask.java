package com.gregswebserver.catan.client.graphics;

/**
 * Created by Greg on 8/14/2014.
 * A diagonal path shape mask.
 */
public class DiagonalDownMask extends RenderMask {

    int height;
    int thickness;

    public DiagonalDownMask(int height, int thickness) {
        this.height = height;
        this.thickness = thickness;
    }

    public int getWidth() {
        return height / 2 + thickness - 1 - thickness / 4;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPadding(int lineNumber) {
        if (lineNumber <= getCenter()) {
            return 2 * (getCenter() - lineNumber + 1) - 1;
        }
        return ((lineNumber - getCenter()) / 2);
    }

    public int getLineWidth(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            int n = 2 * lineNumber + lineNumber / 2 + 3;
            if (n > thickness) return thickness;
            return n;
        }
        return getLineWidth(getHeight() - 1 - lineNumber);
    }

    private int getCenter() {
        return thickness / 4;
    }
}
