package com.gregswebserver.catan.client.graphics.screen;

import com.gregswebserver.catan.client.graphics.graphics.Graphic;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;

/**
 * Created by Greg on 8/19/2014.
 * A ScreenObject that never needs to be re-rendered, and cannot have any child ScreenObjects.
 */
public abstract class GraphicObject extends ScreenObject implements Graphical {

    private Graphic graphic;
    private boolean needsRender;

    protected GraphicObject(int priority) {
        super(priority);
        needsRender = true;
    }

    protected GraphicObject(int priority, Graphic graphic) {
        super(priority);
        setGraphic(graphic);
    }

    public final void setGraphic(Graphic graphic) {
        this.graphic = graphic;
        needsRender = true;
    }

    @Override
    public final boolean needsRender() {
        return needsRender;
    }

    @Override
    public void assertRenderable() {
        if (graphic == null)
            throw new NotYetRenderableException("No graphic assigned to " + this);
    }

    @Override
    public boolean isRenderable() {
        return graphic != null;
    }

    @Override
    public void forceRender() { }

    @Override
    public final Graphic getGraphic() {
        needsRender = false;
        assertRenderable();
        return graphic;
    }

}
