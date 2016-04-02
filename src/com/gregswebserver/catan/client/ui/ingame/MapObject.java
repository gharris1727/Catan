package com.gregswebserver.catan.client.ui.ingame;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.input.UserEvent;

import java.awt.*;

/**
 * Created by greg on 2/20/16.
 * Superclass for map renderables.
 */
public abstract class MapObject extends ScreenRegion {

    private final MapRegion container;

    protected MapObject(int priority, MapRegion container) {
        super(priority);
        this.container = container;
    }

    protected MapRegion getContainer(){
        return container;
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        return container.onMouseDrag(p);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
    }
}