package catan.client.graphics.masks;

import static catan.client.graphics.masks.InvertedMask.Direction.RIGHT;

/**
 * Created by Greg on 12/31/2014.
 * Mask for inverting either the left or right sides of another render mask.
 */
public class InvertedMask extends RenderMask {

    public InvertedMask(RenderMask other, Direction direction) {
        int first = -1;
        int last = 0;
        int leftWidth = 0;
        int minTot = other.width;
        for (int i = 0; i < other.height; i++) {
            int pad = other.padding[i];
            int tot = pad + other.widths[i];
            if (pad > leftWidth)
                leftWidth = pad;
            if (tot < minTot)
                minTot = tot;
            if (tot != other.width) {
                if (first == -1)
                    first = i;
                last = i;
            }
        }
        if (first == -1) {
            setSize(0,0);
        } else {
            if (direction == RIGHT) {
                setSize(other.width - minTot, last - first + 1);
            } else {
                setSize(leftWidth, last - first + 1);
            }
        }
        for (int i = 0; i < height; i++) {
            if (direction == RIGHT) {
                padding[i] = other.padding[i] + other.widths[i];
                widths[i] = width - padding[i];
            } else {
                widths[i] = other.padding[i];
            }
        }
        init();
    }

    public enum Direction {
        LEFT, RIGHT
    }
}
