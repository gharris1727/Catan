package com.gregswebserver.catan.client.graphics.ui.text;

import com.gregswebserver.catan.client.event.UserEvent;
import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.style.UIStyle;
import com.gregswebserver.catan.client.graphics.ui.util.TiledBackground;

import java.awt.event.KeyEvent;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public abstract class TextBox extends UIScreenRegion {

    private final StringBuilder text;

    private final TiledBackground background;
    private final TextLabel label;

    public TextBox(int priority) {
        super(priority);
        text = new StringBuilder();
        background = new TiledBackground(0, UIStyle.BACKGROUND_TEXT) {
            @Override
            public String toString() {
                return "TextBoxBackground";
            }
        };
        label = new TextLabel(1, UIStyle.FONT_PARAGRAPH, "") {
            @Override
            public String toString() {
                return "TextBoxTextLabel";
            }
        };
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
        } else if (Character.isAlphabetic(typed)) {
            text.append(typed);
        }
        label.setText(text.toString());
        forceRender();
        return null;
    }

    public abstract UserEvent onAccept();

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        label.setPosition(getCenteredPosition(label.getGraphic().getMask()));
    }

    public String getString() {
        return text.toString();
    }

}
