package catan.client.graphics.ui;

import catan.client.graphics.masks.RenderMask;
import catan.client.input.UserEventListener;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * Created by Greg on 1/16/2015.
 * A button visible on screen.
 */
public class Button extends DefaultConfigurableScreenRegion {

    private final TiledBackground background;
    private final TextLabel text;
    private final Consumer<UserEventListener> action;

    public Button(String name, int priority, String configKey, String label, Consumer<UserEventListener> action) {
        super(name, priority, configKey);
        this.action = action;
        background = new EdgedTiledBackground();
        text = new TextLabel(name + "Label", 1, "label", label);
        add(background).setClickable(this);
        add(text).setClickable(this);
    }

    @Override
    public void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public void onMouseClick(UserEventListener listener, MouseEvent event) {
        action.accept(listener);
    }

    @Override
    protected void renderContents() {
        center(text);
    }
}
