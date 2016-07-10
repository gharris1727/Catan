package catan.client.ui.game.playing;

import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.GraphicObject;
import catan.client.graphics.ui.*;
import catan.client.input.UserEventListener;
import catan.common.game.util.EnumAccumulator;
import catan.common.resources.GraphicSet;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by greg on 6/18/16.
 * Region to
 */
public class EnumCounterEditRegion<T extends Enum<T>> extends ConfigurableScreenRegion {

    private final TextLabel count;
    private final EnumAccumulator<T> counter;
    private final T instance;
    private final TiledBackground background;
    private final GraphicObject icon;
    private GraphicSet icons;

    EnumCounterEditRegion(String name, int priority, String configKey, EnumAccumulator<T> counter, T instance) {
        super(name, priority, configKey);
        //Store instance information
        this.counter = counter;
        this.instance = instance;
        //Create sub-regions
        enableTransparency();
        background = new EdgedTiledBackground();
        icon = new GraphicObject("ResourceCounterImage", 1, null);
        count = new TextLabel("ResourceCounterCount", 2, "count", "" + counter.get(instance));
        //Add everything to the screen.
        add(background).setClickable(this);
        add(icon).setClickable(this);
        add(count).setClickable(this);
    }

    @Override
    public void onMouseClick(UserEventListener listener, MouseEvent event) {
        switch (event.getButton()) {
            case MouseEvent.BUTTON1: //LEFT
                counter.increment(instance, 1);
                break;
            case MouseEvent.BUTTON2: //MIDDLE
                counter.clear(instance);
                break;
            case MouseEvent.BUTTON3: //RIGHT
                counter.decrement(instance, 1);
                break;
        }
        forceRender();
    }

    @Override
    public void loadConfig(UIConfig config) {
        icons = new GraphicSet(config.getLayout(), "icons", null);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    protected void renderContents() {
        setMask(icons.getMask());
        assertRenderable();
        icon.setGraphic(icons.getGraphic(instance.ordinal()));
        count.setText("" + counter.get(instance));
        count.setPosition(new Point(0, icons.getMask().getHeight() - count.getGraphic().getMask().getHeight()));
    }
}
