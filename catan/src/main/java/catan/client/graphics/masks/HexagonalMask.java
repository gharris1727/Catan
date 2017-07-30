package catan.client.graphics.masks;

import java.awt.Dimension;

import static catan.client.graphics.masks.SymmetricMask.Direction.HORIZONTAL;

/**
 * Created by Greg on 8/14/2014.
 * Hexagon shaped mask that has flats up and down, and angles to the side.
 */
public class HexagonalMask extends CachedMask {

    public HexagonalMask(Dimension size) {
        super(new SymmetricMask(HORIZONTAL, new RenderMask() {
            @Override
            public int getWidth() {
                return size.width;
            }

            @Override
            public int getHeight() {
                return size.height;
            }

            @Override
            public int getLinePadding(int line) {
                return (size.width - getLineWidth(line)) / 2;
            }

            @Override
            public int getLineWidth(int line) {
                return size.width - (2 * (((getHeight() / 2) - line) / 2));
            }
        }));
    }
}

