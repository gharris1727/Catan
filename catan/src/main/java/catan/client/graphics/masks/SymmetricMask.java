package catan.client.graphics.masks;

import static catan.client.graphics.masks.SymmetricMask.Direction.VERTICAL;

/**
 * Created by greg on 7/30/17.
 * A RenderMask that mirrors all requests for the lower-half of a render mask from the upper half.
 */
class SymmetricMask implements RenderMask {

    private final RenderMask other;
    private final Direction direction;

    public SymmetricMask(Direction direction, RenderMask other) {
        this.other = other;
        this.direction = direction;
    }

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
        if (direction == VERTICAL) {
            return other.getLinePadding(line);
        } else {
            return other.getLinePadding(((line * 2) < getHeight()) ? line : getHeight() - line - 1);
        }
    }

    @Override
    public int getLineWidth(int line) {
        if (direction == VERTICAL) {
            return Math.max(0, other.getWidth() - 2 * getLinePadding(line));
        } else {
            return other.getLineWidth(((line * 2) < getHeight()) ? line : getHeight() - line - 1);
        }
    }

    public enum Direction {
        VERTICAL, HORIZONTAL
    }
}
