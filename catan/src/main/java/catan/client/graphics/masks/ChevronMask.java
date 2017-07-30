package catan.client.graphics.masks;

import java.awt.Dimension;

import static catan.client.graphics.masks.SymmetricMask.Direction.HORIZONTAL;

/**
 * Created by greg on 3/13/16.
 * A chevron shaped mask that is horizontally symmetric
 */
public class ChevronMask extends CachedMask {

    public ChevronMask(Dimension size) {
        this(size, size.width/4);
    }

    public ChevronMask(Dimension size, int centerOffset) {
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
                return (centerOffset * line * 2) / size.height;
            }

            @Override
            public int getLineWidth(int line) {
                return size.width - centerOffset;
            }
        }));
    }
}
