package catan.client.graphics.masks;

import java.awt.Dimension;

import static catan.client.graphics.masks.SymmetricMask.Direction.HORIZONTAL;

/**
 * Created by Greg on 8/14/2014.
 * Triangular shaped mask that faces to the left
 */
public class TriangularMask extends CachedMask {

    public TriangularMask(Dimension d) {
        this(d.height);
    }

    public TriangularMask(int height) {
        super(new SymmetricMask(HORIZONTAL, new RenderMask() {
            private int triGroup = ((height - 1) % 4) / 2;

            @Override
            public int getWidth() {
                return (height == 0) ? 0 : ((height - 1) / 4) * 3 + 2 + triGroup;
            }

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public int getLinePadding(int line) {
                return getWidth() - getLineWidth(line);
            }

            @Override
            public int getLineWidth(int line) {
                return line + (line/2) + triGroup;
            }
        }));
    }
}
