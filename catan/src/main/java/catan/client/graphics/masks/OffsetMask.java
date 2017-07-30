package catan.client.graphics.masks;

import java.awt.Point;

/**
 * Created by Greg on 8/17/2014.
 * A mask that has the same profile as another mask, but everything is offset from the corner.
 */
public class OffsetMask extends CachedMask {

    public OffsetMask(RenderMask other, Point offset) {
        super(new RenderMask() {
            @Override
            public int getWidth() {
                return other.getWidth() + offset.x;
            }

            @Override
            public int getHeight() {
                return other.getHeight() + offset.y;
            }

            @Override
            public int getLinePadding(int line) {
                return Math.max(0, other.getLinePadding(line) - offset.x);
            }

            @Override
            public int getLineWidth(int line) {
                return other.getLineWidth(line) + Math.max(0, other.getLinePadding(line) - offset.x);
            }
        });
    }
}
