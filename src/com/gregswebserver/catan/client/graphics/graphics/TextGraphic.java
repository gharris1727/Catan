package com.gregswebserver.catan.client.graphics.graphics;

import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.ui.UIConfig;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Created by greg on 1/25/16.
 * A Graphic that has text written on it.
 */
public class TextGraphic extends Graphic {

    public TextGraphic(UIConfig style, String textStyleName, String text) {
        //Get the text style from the UIConfig
        UIConfig.TextStyle textStyle = style.getTextStyle(textStyleName);
        //Get the font we should be using to render this text.
        Font f = textStyle.getFont();
        //Get the layout for no anti-aliasing.
        FontRenderContext frc = new FontRenderContext(null, false, false);
        //Keep track of the width and height of the text we are assembling.
        int width = 0;
        int height = 0;
        //Iterate over all lines, separated by newline.
        for (String line : text.split("\\r?\\n")) {
            TextLayout layout = new TextLayout(line, f, frc);
            //Figure out the width and height of this image.
            Rectangle2D bounds = f.getStringBounds(line, frc);
            //Add the ascent spacing.
            height += (int) layout.getAscent() + 1;
            //Calculate the line width
            int linewidth = (int) bounds.getWidth() + 1;
            //Keep the largest line width.
            if (width < linewidth)
                width = linewidth;
            //Add on the trailing spacing.
            height += (int) layout.getDescent() + textStyle.getLineSpacing() + 1;
        }
        height -= textStyle.getLineSpacing();
        //Initialize this object with transparency.
        init(new RectangularMask(new Dimension(width, height)), true);
        //Reset to the top of the image again.
        height = 0;
        //Fill the background with transparent pink.
        clear();
        //Initialize the draw graphics.
        Graphics g = buffer.getGraphics();
        g.setColor(textStyle.getColor());
        //Iterate over all lines, this time drawing the words.
        for (String line : text.split("\\r?\\n")) {
            TextLayout layout = new TextLayout(line, f, frc);
            //Add the ascent spacing.
            height += (int) layout.getAscent() + 1;
            //Always left justified... does it look like i can right justify???
            //TODO: see if complicated justification is worth the effort.
            //Draw the text onto the graphic.
            layout.draw((Graphics2D) g, 0, height);
            //Add on the trailing spacing.
            height += (int) layout.getDescent() + textStyle.getLineSpacing() + 1;
        }
    }
}
