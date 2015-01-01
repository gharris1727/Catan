package com.gregswebserver.catan.client.masks;

/**
 * Created by Greg on 8/19/2014.
 * Takes another render mask and becomes the mirrored version of it.
 */
public class FlippedMask extends RenderMask {

    public FlippedMask(RenderMask other, Direction direction) {
        width = other.width;
        height = other.height;
        switch (direction) {
            case VERTICAL:
                padding = new int[height];
                widths = new int[height];
                for (int i = 0; i < height; i++) {
                    padding[i] = other.padding[height - 1 - i];
                    widths[i] = other.widths[height - 1 - i];
                }
                break;
            case HORIZONTAL:
            default:
                padding = new int[height];
                widths = other.widths;
                for (int i = 0; i < height; i++) {
                    padding[i] = width - other.padding[i] - other.widths[i];
                }
                break;
        }
        init();
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }
}
