package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.graphics.graphics.BufferedGraphic;
import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.graphics.masks.RectangularMask;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.GraphicObject;
import com.gregswebserver.catan.client.graphics.ui.style.Styled;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Created by greg on 1/12/16.
 * A graphical text label
 */
public abstract class TextLabel extends GraphicObject implements Styled {

    private final String textStyleName;

    private UIStyle style;
    private String text;

    private BufferedGraphic graphic;
    private TextLayout layout;

    public TextLabel(int priority, String textStyleName, String text) {
        super(priority);
        this.textStyleName = textStyleName;
        setText(text);
    }

    @Override
    public void setStyle(UIStyle style) {
        this.style = style;
        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        if (style != null && text != null && text.length() > 0) {
            Font f = style.getTextStyle(textStyleName).getFont();
            FontRenderContext frc = new FontRenderContext(null, false, false);
            layout = new TextLayout(text, f, frc);
            Rectangle2D bounds = f.getStringBounds(text, frc);
            int width = (int) bounds.getWidth() + 1;
            int height = (int) layout.getAscent() + (int) layout.getDescent() + 1;
            RenderMask mask = new RectangularMask(new Dimension(width, height));
            graphic = new BufferedGraphic(mask);
            Graphics g = graphic.getDrawGraphics();
            g.setColor(style.getTextStyle(textStyleName).getColor());
            layout.draw((Graphics2D) g, 0, (int) layout.getAscent());
            g.dispose();
            setGraphic(graphic);
        } else {
            setGraphic(new Graphic(new RectangularMask(new Dimension(0, 0))));
        }
    }

    public int getCharIndex(Point p) {
        float x = p.x - layout.getAdvance();
        float y = p.y - layout.getAscent();
        TextHitInfo hit = layout.hitTestChar(x, y);
        return hit.getCharIndex();
    }

    @Override
    public UIStyle getStyle() {
        return style;
    }
}
