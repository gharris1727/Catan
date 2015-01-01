package com.gregswebserver.catan.client.masks;

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
            height = 0;
            width = 0;
        } else {
            height = last - first + 1;
            switch (direction) {
                case RIGHT:
                    width = other.width - minTot;
                    break;
                case LEFT:
                default:
                    width = leftWidth;
                    break;
            }
        }
        padding = new int[height];
        widths = new int[height];
        for (int i = 0; i < height; i++) {
            switch (direction) {
                case RIGHT:
                    padding[i] = other.padding[i] + other.widths[i];
                    widths[i] = width - padding[i];
                    break;
                default:
                case LEFT:
                    widths[i] = other.padding[i];
                    break;
            }
        }
        init();
    }

    public enum Direction {
        LEFT, RIGHT
    }
}
