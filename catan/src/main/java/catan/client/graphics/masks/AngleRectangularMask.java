package catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * A diagonal path shape mask.
 */
public class AngleRectangularMask extends RenderMask {

    public AngleRectangularMask(Dimension size) {
        this(size, new Dimension((8 * size.width) / 10, (8 * size.height) / 10));
    }

    public AngleRectangularMask(Dimension size, Dimension ins) {
        setSize(size.width, size.height);
        double w = size.getWidth();
        double h = size.getHeight();
        double x = ins.getWidth();
        double y = ins.getHeight();
        for (int i = 0; i < (size.height - ins.height); i++) {
            padding[i] = (int) ((x*(y-i))/y); // f(i)
            widths[i] = width - padding[i] - (int) ((((w - x) * (h - y - i)) / (h - y))); // g(i)
        }
        for (int i = size.height - ins.height; i < ins.height; i++) {
            padding[i] = (int) ((x*(y-i))/y); // f(i)
            widths[i] = width - padding[i] - (int) ((x*(i-(h-y)))/y); // j(i)
        }
        for (int i = ins.height; i < height; i++) {
            padding[i] = (int) (((w-x)*(i-y))/(h-y)); // h(i)
            widths[i] = width - padding[i] - (int) ((x*(i-(h-y)))/y); // j(i)
        }
        init();
    }
}
