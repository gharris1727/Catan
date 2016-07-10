package catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * Helper class to generate iterators describing the masking on a hexagonal sprite.
 * Warning: most of what happens in this class is magic and needs the test class to be sure it works.
 */
public class HexagonalMask extends RenderMask {


    public HexagonalMask(Dimension size) {
        width = size.width;
        height = size.height;
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
            return (width - getLineWidth(lineNumber)) / 2;
        }
        return getLeftPadding(getHeight() - 1 - lineNumber);
    }

    private int getLineWidth(int lineNumber) {
        if (lineNumber * 2 < getHeight()) {
            return width - 2 * (getCenter(lineNumber) / 2);
        }
        return getLineWidth(getHeight() - 1 - lineNumber);
    }

    private int getCenter(int lineNumber) {
        return getHeight() / 2 - lineNumber;
    }
}

