package com.gregswebserver.catan.client.graphics.util;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by Greg on 8/24/2014.
 * graphical object that takes a text string and displays it.
 */
public class TextGraphic extends Graphic {

    private TextLayout layout;
    private FontMetrics metrics;

    public TextGraphic(Font f, String s) {
        FontRenderContext frc = new FontRenderContext(null, true, true);
        layout = new TextLayout(s, f, frc);
        Rectangle2D bounds = layout.getBounds();
        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();
        BufferedImage image;
        image = new BufferedImage(width, height, TYPE_INT_RGB);
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        setMask(new RectangularMask(new Dimension(width, height)));
        clear();
        Graphics g = image.getGraphics();
        g.setFont(f);
        metrics = g.getFontMetrics();
        g.drawString(s, 0, (int) layout.getAscent());
        g.dispose();
    }

    public TextLayout getLayout() {
        return layout;
    }

    public FontMetrics getMetrics() {
        return metrics;
    }
}
