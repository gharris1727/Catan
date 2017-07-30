package catan.client.graphics.masks;

import java.awt.Dimension;

/**
 * Created by Greg on 8/14/2014.
 * A rectangular mask rotated by an angle.
 */
public class AngleRectangularMask extends CachedMask {

    public AngleRectangularMask(Dimension size) {
        this(size, new Dimension((8 * size.width) / 10, (8 * size.height) / 10));
    }

    public AngleRectangularMask(Dimension size, Dimension ins) {
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
                if (line < (size.height - ins.height)) {
                    return (int) ((ins.getWidth() * (ins.getHeight() - line)) / ins.getHeight()); // f(i)
                } else if (line < ins.height) {
                    return (int) ((ins.getWidth() * (ins.getHeight() - line)) / ins.getHeight()); // f(i)
                } else if (line < size.height) {
                    return (int) (((size.getWidth() - ins.getWidth()) * (line - ins.getHeight())) / (size.getHeight() - ins.getHeight())); // h(i)
                } else {
                    return 0;
                }
            }

            @Override
            public int getLineWidth(int line) {
                if (line < (size.height - ins.height)) {
                    return size.width - getLinePadding(line) - (int) ((((size.getWidth() - ins.getWidth()) * (size.getHeight() - ins.getHeight() - line)) / (size.getHeight() - ins.getHeight()))); // g(i)
                } else if (line < ins.height) {
                    return size.width - getLinePadding(line) - (int) ((ins.getWidth() * (line - (size.getHeight() - ins.getHeight()))) / ins.getHeight()); // j(i)
                } else if (line < size.height) {
                    return size.width - getLinePadding(line) - (int) ((ins.getWidth() * (line - (size.getHeight() - ins.getHeight()))) / ins.getHeight()); // j(i)
                } else {
                    return 0;
                }
            }
        });
    }
}
