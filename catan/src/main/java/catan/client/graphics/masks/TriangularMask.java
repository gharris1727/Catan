package catan.client.graphics.masks;

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
        triGroup = (height - 1) % 4;
        setSize((height == 0) ? height : ((height - 1) / 4) * 3 + 2 + triGroup / 2, height);
        for (int i = 0; i < height; i++) {
            padding[i] = getLeftPadding(i);
            widths[i] = getLineWidth(i);
        }
        init();
    }

    private int getLeftPadding(int lineNumber) {
        if ((lineNumber * 2) > getHeight()) {
            lineNumber = getHeight() - 1 - lineNumber;
        }
        return getWidth() - getLineWidth(lineNumber);
    }

    private int getLineWidth(int lineNumber) {
        if ((lineNumber * 2) > getHeight()) {
            lineNumber = getHeight() - 1 - lineNumber;
        }
        switch (triGroup) {
            case 0:
            case 1:
                return (lineNumber + (lineNumber / 2) + 2);
            case 2:
            case 3:
            default:
                return (lineNumber + ((lineNumber + 1) / 2) + 1);
        }
    }
}
