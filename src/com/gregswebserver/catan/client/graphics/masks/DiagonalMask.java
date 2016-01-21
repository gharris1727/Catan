package com.gregswebserver.catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * A diagonal path shape mask.
 */
public class DiagonalMask extends RenderMask {

    //TODO: Re-implement.

    private final int thickness;

    public DiagonalMask(Dimension d) {
        thickness = d.width;
        width = height / 2 + thickness - 2 - thickness / 3;
        height = d.height;
        padding = new int[height];
        widths = new int[height];
        for (int i = 0; i < height; i++) {
            padding[i] = getLeftPadding(i);
            widths[i] = getLineWidth(i);
        }
        init();
    }

    private int getLeftPadding(int lineNumber) {
        if (lineNumber < getCenter()) {
            return ((getCenter() - lineNumber + 1) / 2);
        }
        return (lineNumber - getCenter()) + (lineNumber - getCenter()) / 2;
    }

    private int getLineWidth(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            int n = 3 + lineNumber + 2 * (lineNumber / 2);
            if (n > thickness) return thickness;
            return n;
        }
        return getLineWidth(getHeight() - 1 - lineNumber);
    }

    private int getCenter() {
        return height - thickness / 3 - 3;
    }
}
