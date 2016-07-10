package catan.client.graphics.ui;

import catan.client.graphics.masks.RectangularMask;
import catan.client.graphics.masks.RenderMask;
import catan.client.graphics.screen.ScreenObject;

import java.awt.*;

/**
 * Created by greg on 6/5/16.
 * Superclass of a dynamically sized scrolling block of screen regions.
 */
public abstract class ScrollingList extends ScrollingScreenRegion {

    private RenderMask elementSize;

    protected ScrollingList(String name, int priority, String configKey) {
        super(name, priority, configKey);
    }

    public void setElementSize(RenderMask elementSize) {
        this.elementSize = elementSize;
    }

    @Override
    protected void renderContents() {
        Point position = new Point();
        int width = 0;
        for (ScreenObject element : this) {
            if (element instanceof Configurable)
                ((Configurable) element).setConfig(getConfig());
            RenderMask elementMask;
            if (elementSize != null && element instanceof Resizable) {
                ((Resizable) element).setMask(elementSize);
                elementMask = elementSize;
            } else {
                elementMask = element.getGraphic().getMask();
            }
            if (width < elementMask.getWidth())
                width = elementMask.getWidth();
            element.setPosition(position);
            position.translate(0, elementMask.getHeight());
        }
        setMask(new RectangularMask(new Dimension(width, position.y)));
    }
}
