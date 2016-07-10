package catan.client.graphics.masks;

import java.awt.*;

/**
 * Created by Greg on 8/21/2014.
 * A mask that traces a circular/oval shape
 * This behaves differently than other render masks, where it pre-generates the array
 * and uses that to look up values, rather than generating the array from the look-ups.
 */
public class RoundMask extends RenderMask {

    public RoundMask(Dimension size) {
        width = size.width;
        height = size.height;
        padding = new int[height];
        widths = new int[height];
        generate((width - 1) / 2, (height - 1) / 2, (width - 1) / 2, (height - 1) / 2);
        init();
    }

    private void generate(int xc, int yc, int a, int b) {
        //TODO: parse this function.
        int x = 0, y = b;
        int width = 1;
        long a2 = (long) a * a, b2 = (long) b * b;
        long crit1 = -(a2 / 4 + a % 2 + b2);
        long crit2 = -(b2 / 4 + b % 2 + a2);
        long crit3 = -(b2 / 4 + b % 2);
        long t = -a2 * y; /* e(x+1/2,y-1/2) - (a^2+b^2)/4 */
        long dxt = 2 * b2 * x, dyt = -2 * a2 * y;
        long d2xt = 2 * b2, d2yt = 2 * a2;

        while (y >= 0 && x <= a) {
            if (t + b2 * x <= crit1 ||     /* e(x+1,y-1/2) <= 0 */
                    t + a2 * y <= crit3) {     /* e(x+1/2,y) <= 0 */
                x++;
                dxt += d2xt;
                t += dxt;
                width += 2;
            } else if (t - a2 * y > crit2) { /* e(x+1/2,y-1) > 0 */
                row(xc - x, yc - y, width);
                if (y != 0)
                    row(xc - x, yc + y, width);
                y--;
                dyt += d2yt;
                t += dyt;
            } else {
                row(xc - x, yc - y, width);
                if (y != 0)
                    row(xc - x, yc + y, width);
                x++;
                dxt += d2xt;
                t += dxt;
                y--;
                dyt += d2yt;
                t += dyt;
                width += 2;
            }
        }
        if (b == 0)
            row(xc - a, yc, 2 * a + 1);
    }

    private void row(int x, int y, int len) {
        padding[y] = x;
        widths[y] = len;
    }
}
