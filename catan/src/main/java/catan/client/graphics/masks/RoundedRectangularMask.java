package catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/21/2014.
 * A rectangular mask except the corners are rounded off
 */
public class RoundedRectangularMask extends RenderMask {

    private static final int SCALE = 4;

    private RenderMask corner;

    public RoundedRectangularMask(Dimension square) {
        //Auto-sizes the corners to be proportional to the whole window.

        int radius = Math.min(square.width, square.height) / SCALE;
        init(square, new Dimension(radius, radius));
    }

    public RoundedRectangularMask(Dimension square, Dimension corner) {
        //Uses the external dimensions given for customization.
        init(square, corner);
    }

    private void init(Dimension square, Dimension corner) {
        width = square.width;
        height = square.height;
        this.corner = new RoundMask(corner);
        padding = new int[height];
        widths = new int[height];
        for (int i = 1; i < height; i++) {
            padding[i] = getPadding(i);
            widths[i] = getWidth(i);
        }
        init();
    }

    private int getPadding(int lineNumber) {
        if (lineNumber < (corner.getHeight() / 2)) {
            return corner.padding[lineNumber];
        } else if ((getHeight() - 1 - lineNumber) < (corner.getHeight() / 2)) {
            return corner.padding[getHeight() - 1 - lineNumber];
        } else {
            return 0;
        }
    }

    private int getWidth(int lineNumber) {
        return width - (2 * getPadding(lineNumber));
    }
}
