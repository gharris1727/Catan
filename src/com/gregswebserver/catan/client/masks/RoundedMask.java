package com.gregswebserver.catan.client.masks;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Greg on 8/21/2014.
 * A mask that traces a circular/oval shape
 * This behaves differently than other render masks, where it pre-generates the array
 * and uses that to look up values, rather than generating the array from the look-ups.
 */
public class RoundedMask extends RenderMask {

    private ArrayList<Integer> padding, lengths;

    public RoundedMask(Dimension size) {
        padding = new ArrayList<>();
        lengths = new ArrayList<>();
        generate(size.width, size.height, size.width, size.height);
    }

    public int getWidth() {
        return lengths.get(lengths.size() / 2);
    }

    public int getHeight() {
        return lengths.size();
    }

    public int getLeftPadding(int lineNumber) {
        return padding.get(lineNumber);
    }

    public int getLineWidth(int lineNumber) {
        return lengths.get(lineNumber);
    }

    public void generate(int xc, int yc, int a, int b) {
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
        while (padding.size() <= y || lengths.size() <= y) {
            padding.add(0);
            lengths.add(0);
        }
        padding.set(y, x);
        lengths.set(y, len);
    }

    public ArrayList<Integer> getLeftPadding() {
        return padding;
    }

    public ArrayList<Integer> getLineWidth() {
        return lengths;
    }
}
