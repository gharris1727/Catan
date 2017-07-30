package catan.client.graphics.masks;

/**
 * Created by Greg on 8/19/2014.
 * Takes another render mask and becomes the mirrored version of it.
 */
public class FlippedMask extends RenderMask {

    public FlippedMask(RenderMask other, Direction direction) {
        setSize(other.width, other.height);
        if (direction == Direction.VERTICAL) {
            for (int i = 0; i < height; i++) {
                padding[i] = other.padding[height - 1 - i];
                widths[i] = other.widths[height - 1 - i];
            }
        } else {
            for (int i = 0; i < height; i++) {
                padding[i] = width - other.padding[i] - other.widths[i];
                widths[i] = other.widths[i];
            }
        }
        init();
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }
}
