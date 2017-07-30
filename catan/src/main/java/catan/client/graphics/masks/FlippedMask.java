package catan.client.graphics.masks;

/**
 * Created by Greg on 8/19/2014.
 * A mask that is the mirrored version of another mask (horizontal or vertical)
 */
public class FlippedMask extends CachedMask {

    public FlippedMask(RenderMask other, Direction direction) {
        super(new RenderMask() {
            @Override
            public int getWidth() {
                return other.getWidth();
            }

            @Override
            public int getHeight() {
                return other.getHeight();
            }

            @Override
            public int getLinePadding(int line) {
                if (direction == Direction.VERTICAL) {
                    return other.getLinePadding(other.getHeight() - 1 - line);
                } else {
                    return other.getWidth() - other.getLinePadding(line) - other.getLineWidth(line);
                }
            }

            @Override
            public int getLineWidth(int line) {
                if (direction == Direction.VERTICAL) {
                    return other.getLineWidth(other.getHeight() - 1 - line);
                } else {
                    return other.getLineWidth(line);
                }
            }
        });
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }
}
