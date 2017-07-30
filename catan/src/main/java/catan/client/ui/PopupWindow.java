package catan.client.ui;

import catan.client.graphics.ui.ConfigurableScreenRegion;
import catan.client.graphics.ui.ScrollingScreenRegion;
import catan.client.input.UserEventListener;
import catan.client.renderer.RenderEvent;
import catan.client.renderer.RenderEventType;

import java.awt.Point;

/**
 * Created by greg on 4/3/16.
 * A popup window rendered on top of the ClientScreen
 */
public abstract class PopupWindow extends ScrollingScreenRegion {

    private final ConfigurableScreenRegion source;

    protected PopupWindow(String name, String configKey, ConfigurableScreenRegion source) {
        super(name, 2, configKey);
        this.source = source;
        setRenderer(source.getRenderer());
    }

    public void display() {
        getRenderer().addEvent(new RenderEvent(source, RenderEventType.Popup_Show, this));
    }

    public void expire() {
        getRenderer().addEvent(new RenderEvent(source, RenderEventType.Popup_Remove, this));
    }

    @Override
    protected void updateBounds() {
        if (isRenderable()){
            minX = 0;
            minY = 0;
            maxX = getHostMask().getWidth() - getMask().getWidth();
            maxY = getHostMask().getHeight() - getMask().getHeight();
        }
    }

    @Override
    public void onMouseDrag(UserEventListener listener, Point p) {
        scroll(p.x, p.y);
    }
}
