package com.gregswebserver.catan.client.masks;

/**
 * Created by Greg on 8/14/2014.
 * Triangular shaped mask
 * Designed to fit in the space between HexagonalMask sections.
 */
public class TriangularMask extends RenderMask {

    private int height;
    private int triGroup;

    public TriangularMask(int height) {
        this.height = height;
        this.triGroup = (height - 1) % 4;
    }

    public int getWidth() {
        if (height == 0) return 0;
        return ((height - 1) / 4) * 3 + 2 + triGroup / 2;
    }

    public int getHeight() {
        return height;
    }

    public int getLeftPadding(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            return getWidth() - getLineWidth(lineNumber);
        }
        return getLeftPadding(getHeight() - 1 - lineNumber);
    }

    public int getLineWidth(int lineNumber) {
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
