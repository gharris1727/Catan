package com.gregswebserver.catan.client.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * A diagonal path shape mask.
 */
public class DiagonalMask extends RenderMask {

    private final int thickness;
    private final int height;

    public DiagonalMask(Dimension d) {
        this(d.width, d.height);
    }

    public DiagonalMask(int thickness, int height) {
        this.thickness = thickness;
        this.height = height;
    }

    public int getWidth() {
        return height / 2 + thickness - 2 - thickness / 3;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPadding(int lineNumber) {
        if (lineNumber < getCenter()) {
            return ((getCenter() - lineNumber + 1) / 2);
        }
        return (lineNumber - getCenter()) + (lineNumber - getCenter()) / 2;
    }

    public int getLineWidth(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            int n = 3 + lineNumber + 2 * (lineNumber / 2);
            if (n > thickness) return thickness;
            return n;
        }
        return getLineWidth(getHeight() - 1 - lineNumber);
    }

    public int getCenter() {
        return height - thickness / 3 - 3;
    }
}
