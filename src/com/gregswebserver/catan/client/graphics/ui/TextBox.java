package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;

import java.awt.event.KeyEvent;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public abstract class TextBox extends StyledScreenRegion {

    private final StringBuilder text;

    private final TiledBackground background;
    private final TextLabel label;

    public TextBox(int priority, String prepopulate) {
        super(priority);
        text = new StringBuilder();
        background = new TiledBackground(0, UIStyle.BACKGROUND_TEXT);
        label = new TextLabel(1, UIStyle.FONT_PARAGRAPH, prepopulate);
        add(background).setClickable(this);
        add(label).setClickable(this);
    }

    @Override
    public UserEvent onKeyTyped(KeyEvent event) {
        char typed = event.getKeyChar();
        if (typed == '\n')
            return onAccept();
        else if (typed == '\b') {
            int len = text.length() - 1;
            if (len < 0)
                len = 0;
            text.setLength(len);
        } else {
            text.append(typed);
        }
        label.setText(text.toString());
        forceRender();
        return null;
    }

    protected abstract UserEvent onAccept();

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        center(label).x = 16;
    }

    public void setText(String text) {
        this.text.setLength(0);
        this.text.append(text);
        label.setText(text);
    }

    public String getText() {
        return text.toString();
    }

    public int getInt() {
        return Integer.parseInt(text.toString());
    }

    public double getDouble() {
        return Double.parseDouble(text.toString());
    }

}
