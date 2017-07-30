package catan.client.graphics.masks;

import static catan.client.graphics.masks.InvertedMask.Direction.RIGHT;

/**
 * Created by Greg on 12/31/2014.
 * Mask that matches the shape of either the left side or right side of an existing mask.
 */
public class InvertedMask extends CachedMask {

    public InvertedMask(RenderMask other, Direction direction) {
        super(new RenderMask() {

            private int first = -1;
            private int last = 0;
            private int leftWidth = 0;
            private int minTot = other.getWidth();

            {
                for (int i = 0; i < other.getHeight(); i++) {
                    int pad = other.getLinePadding(i);
                    int tot = pad + other.getLineWidth(i);
                    if (pad > leftWidth)
                        leftWidth = pad;
                    if (tot < minTot)
                        minTot = tot;
                    if (tot != other.getWidth()) {
                        if (first == -1)
                            first = i;
                        last = i;
                    }
                }
            }

            @Override
            public int getWidth() {
                if (first == -1) {
                    return 0;
                } else if (direction == RIGHT) {
                    return other.getWidth() - minTot;
                } else {
                    return leftWidth;
                }
            }

            @Override
            public int getHeight() {
                return (first == -1) ? 0 : last - first + 1;
            }

            @Override
            public int getLinePadding(int line) {
                return (direction == RIGHT) ? other.getLinePadding(line) + other.getLineWidth(line) : 0;
            }

            @Override
            public int getLineWidth(int line) {
                return (direction == RIGHT) ? getWidth() - other.getLinePadding(line) : other.getLinePadding(line);
            }
        });
    }

    public enum Direction {
        LEFT, RIGHT
    }
}
