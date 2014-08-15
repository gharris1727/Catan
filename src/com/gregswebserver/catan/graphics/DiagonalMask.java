package com.gregswebserver.catan.graphics;

/**
 * Created by Greg on 8/14/2014.
 * A diagonal path shape mask.
 */
public class DiagonalMask extends RenderMask {

    int height;
    int thickness;

    public DiagonalMask(int height, int thickness) {
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
        if (lineNumber < getCenter()) {
            return ((getCenter() - lineNumber) / 2);
        }
        return 2 * (lineNumber - getCenter() + 1) - 1;
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
        return height - thickness / 4 - 1;
    }
}
