package catan.client.graphics.masks;

import java.awt.Dimension;

import static catan.client.graphics.masks.SymmetricMask.Direction.HORIZONTAL;

/**
 * Created by Greg on 8/21/2014.
 * A rectangular mask except the corners are rounded off
 */
public class RoundedRectangularMask extends CachedMask {

    private static final int SCALE = 4;


    public RoundedRectangularMask(Dimension square) {
        this(square, new Dimension(
                Math.min(square.width, square.height) / SCALE,
                Math.min(square.width, square.height) / SCALE));
    }

    public RoundedRectangularMask(Dimension square, Dimension corner) {
        super(new SymmetricMask(HORIZONTAL, new RenderMask() {
            private RenderMask cornerMask = new RoundMask(corner);

            @Override
            public int getWidth() {
                return square.width;
            }

            @Override
            public int getHeight() {
                return square.height;
            }

            @Override
            public int getLinePadding(int line) {
                return (line*2) > cornerMask.getHeight() ? 0 : cornerMask.getLinePadding(line);
            }

            @Override
            public int getLineWidth(int line) {
                return square.width - (2 * getLinePadding(line));
            }
        }));
    }
}
