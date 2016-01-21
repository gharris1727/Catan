package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;

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

    private final TextLayout layout;

    public TextGraphic(UIStyle.TextStyle style, String text) {
        Font f = style.getFont();
        FontRenderContext frc = new FontRenderContext(null, false, false);
        layout = new TextLayout(text, f, frc);
        Rectangle2D bounds = f.getStringBounds(text, frc);
        int width = (int) bounds.getWidth();
        int height = (int) layout.getAscent() + (int) layout.getDescent();
        BufferedImage image = new BufferedImage(width, height, TYPE_INT_RGB);
        setPixels(((DataBufferInt) image.getRaster().getDataBuffer()).getData());
        setMask(new RectangularMask(new Dimension(width, height)));
        clear();
        Graphics g = image.getGraphics();
        g.setColor(style.getColor());
        layout.draw((Graphics2D) g, 0, (int) layout.getAscent());
        g.dispose();
    }

    public TextLayout getLayout() {
        return layout;
    }

}
