package catan.client.graphics.masks;

import java.awt.Dimension;

import static catan.client.graphics.masks.SymmetricMask.Direction.HORIZONTAL;

/**
 * Created by Greg on 8/21/2014.
 * A circle/elliptical mask that traces a rounded shape.
 */
public class RoundMask extends CachedMask {

    public RoundMask(Dimension size) {
        super(new SymmetricMask(HORIZONTAL, new RenderMask() {

            private int[] padding = new int[size.height];
            private int[] widths = new int[size.height];

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
                return padding[line];
            }

            @Override
            public int getLineWidth(int line) {
                return widths[line];
            }

            {
                int horizRadius = (size.width - 1) / 2;
                int vertRadius = (size.height - 1) / 2;

                if (vertRadius == 0 && size.height != 0) {
                    row(0, 0, size.width);
                } else {
                    //TODO: parse this function.
                    int x = 0;
                    int y = vertRadius;
                    int width = 1;
                    long horizRadiusSquared = (long) horizRadius * horizRadius;
                    long vertRadiusSquared = (long) vertRadius * vertRadius;
                    long crit1 = -((horizRadiusSquared / 4) + (horizRadius % 2) + vertRadiusSquared);
                    long crit2 = -((vertRadiusSquared / 4) + (vertRadius % 2) + horizRadiusSquared);
                    long crit3 = -((vertRadiusSquared / 4) + (vertRadius % 2));
                    long t = -horizRadiusSquared * y; /* e(x+1/2,y-1/2) - (a^2+b^2)/4 */
                    long dxt = 2 * vertRadiusSquared * x;
                    long dyt = -2 * horizRadiusSquared * y;
                    long d2xt = 2 * vertRadiusSquared;
                    long d2yt = 2 * horizRadiusSquared;

                    while ((y >= 0) && (x <= horizRadius)) {
                        if (((t + (vertRadiusSquared * x)) <= crit1) ||     /* e(x+1,y-1/2) <= 0 */
                                ((t + (horizRadiusSquared * y)) <= crit3)) {     /* e(x+1/2,y) <= 0 */
                            x++;
                            dxt += d2xt;
                            t += dxt;
                            width += 2;
                        } else if ((t - (horizRadiusSquared * y)) > crit2) { /* e(x+1/2,y-1) > 0 */
                            row(horizRadius - x, vertRadius - y, width);
                            if (y != 0)
                                row(horizRadius - x, vertRadius + y, width);
                            y--;
                            dyt += d2yt;
                            t += dyt;
                        } else {
                            row(horizRadius - x, vertRadius - y, width);
                            if (y != 0)
                                row(horizRadius - x, vertRadius + y, width);
                            x++;
                            dxt += d2xt;
                            t += dxt;
                            y--;
                            dyt += d2yt;
                            t += dyt;
                            width += 2;
                        }
                    }
                }
            }

            private void row(int x, int y, int len) {
                padding[y] = x;
                widths[y] = len;
            }
        }));
    }

}
