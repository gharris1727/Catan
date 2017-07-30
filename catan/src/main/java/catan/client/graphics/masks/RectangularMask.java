package catan.client.graphics.masks;

import java.awt.Dimension;

/**
 * Created by Greg on 8/14/2014.
 * Generic mask for use with any normal rectangular/square sprite.
 */
public class RectangularMask extends CachedMask {

    public RectangularMask(Dimension size) {
        super(new RenderMask() {
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
                return 0;
            }

            @Override
            public int getLineWidth(int line) {
                return size.width;
            }
        });
    }

    @Override
    public boolean isAccelerable() {
        return true;
    }
}
