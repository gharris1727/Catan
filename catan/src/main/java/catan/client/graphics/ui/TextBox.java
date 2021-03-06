package catan.client.graphics.ui;

import catan.client.graphics.masks.RenderMask;
import catan.client.input.UserEventListener;

import java.awt.event.KeyEvent;
import java.util.function.BiConsumer;

/**
 * Created by Greg on 1/3/2015.
 * A screen area containing a text box that is editable by the user.
 */
public class TextBox extends DefaultConfigurableScreenRegion {

    //TODO: selecting text and inserting in the middle of a string.

    private final StringBuilder text;

    private final TiledBackground background;
    private final TextLabel label;
    private final BiConsumer<UserEventListener, String> accept;

    public TextBox(String name, int priority, String configKey, String prepopulate, boolean keepExisting, BiConsumer<UserEventListener, String> accept) {
        super(name, priority, configKey);
        this.accept = accept;
        text = new StringBuilder();
        background = new TiledBackground();
        label = new TextLabel(name + "Label", 1, "label", prepopulate);
        if (keepExisting)
            text.append(prepopulate);
        add(background).setClickable(this);
        add(label).setClickable(this);
    }

    @Override
    public void onKeyTyped(UserEventListener listener, KeyEvent event) {
        char typed = event.getKeyChar();
        if (typed == '\n')
            accept.accept(listener, text.toString());
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
    }

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
