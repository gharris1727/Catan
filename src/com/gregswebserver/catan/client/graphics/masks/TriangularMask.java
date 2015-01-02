package com.gregswebserver.catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * Triangular shaped mask
 * Designed to fit in the space between HexagonalMask sections.
 */
public class TriangularMask extends RenderMask {

    private final int triGroup;

    public TriangularMask(Dimension d) {
        this(d.height);
    }

    public TriangularMask(int height) {
        this.triGroup = (height - 1) % 4;
        this.height = height;
        this.width = (height == 0) ? height : ((height - 1) / 4) * 3 + 2 + triGroup / 2;
        padding = new int[height];
        widths = new int[height];
        for (int i = 0; i < height; i++) {
            padding[i] = getLeftPadding(i);
            widths[i] = getLineWidth(i);
        }
        init();
    }

    private int getLeftPadding(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            return getWidth() - getLineWidth(lineNumber);
        }
        return getLeftPadding(getHeight() - 1 - lineNumber);
    }

    private int getLineWidth(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            switch (triGroup) {
                case 0:
                case 1:
                    return (lineNumber + (lineNumber / 2) + 2);
                case 2:
                case 3:
                default:
                    return (lineNumber + ((lineNumber + 1) / 2) + 1);
            }
//            return (lineNumber + ((lineNumber + lineNumber % 2) / 2) + 2 - (lineNumber % 2));
        }
        return getLineWidth(getHeight() - 1 - lineNumber);
    }
}
