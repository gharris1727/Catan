package catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/14/2014.
 * A diagonal path shape mask.
 */
public class AngleRectangularMask extends RenderMask {

    public AngleRectangularMask(Dimension d) {
        this(d, new Dimension(8*d.width/10, 8*d.height/10));
    }

    public AngleRectangularMask(Dimension d, Dimension ins) {
        width = d.width;
        height = d.height;
        padding = new int[height];
        widths = new int[height];
        double w = d.getWidth();
        double h = d.getHeight();
        double x = ins.getWidth();
        double y = ins.getHeight();
        for (int i = 0; i < d.height - ins.height; i++) {
            padding[i] = (int) ((x*(y-i))/y); // f(i)
            widths[i] = width - padding[i] - (int) (((w-x)*(h-y-i)/(h-y))); // g(i)
        }
        for (int i = d.height - ins.height; i < ins.height; i++) {
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
