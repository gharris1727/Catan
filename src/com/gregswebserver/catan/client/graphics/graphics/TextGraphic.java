package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Created by greg on 1/25/16.
 * A Graphic that has text written on it.
 */
public class TextGraphic extends Graphic {

    public TextGraphic(UIStyle style, String textStyleName, String text) {
        //Get the font we should be using to render this text.
        Font f = style.getTextStyle(textStyleName).getFont();
        //Get the layout for no anti-aliasing.
        FontRenderContext frc = new FontRenderContext(null, false, false);
        TextLayout layout = new TextLayout(text, f, frc);
        //Figure out the width and height of this image.
        Rectangle2D bounds = f.getStringBounds(text, frc);
        int width = (int) bounds.getWidth() + 1;
        int height = (int) layout.getAscent() + (int) layout.getDescent() + 1;
        //Initialize this object with transparency.
        init(new RectangularMask(new Dimension(width, height)), true);
        //Fill the background with transparent pink.
        clear();
        //Draw the text onto the graphic.
        Graphics g = buffer.getGraphics();
        g.setColor(style.getTextStyle(textStyleName).getColor());
        layout.draw((Graphics2D) g, 0, (int) layout.getAscent());
    }
}
