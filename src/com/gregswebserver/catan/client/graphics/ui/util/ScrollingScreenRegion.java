package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;

import java.awt.*;

/**
 * Created by greg on 1/21/16.
 * A screen region that supports smooth scrolling, and keeps within specified bounds.
 */
public abstract class ScrollingScreenRegion extends UIScreenRegion {

    private RenderMask hostMask;

    public ScrollingScreenRegion(int priority) {
        super(priority);
    }

    public void setHostMask(RenderMask hostMask) {
        this.hostMask = hostMask;
        checkBounds();
    }

    public void scroll(int x, int y) {
        getPosition().translate(x, y);
        checkBounds();
    }

    protected void checkBounds() {
        if (isRenderable()){
            Point position = getPosition();
            Dimension size = getMask().getSize();
            Dimension host = hostMask.getSize();
            int minX = host.width - size.width;
            int minY = host.height - size.height;
            boolean needsRender = needsRender();
            if (needsRender |= (position.x < minX))
                position.x = minX;
            if (needsRender |= (position.y < minY))
                position.y = minY;
            if (needsRender |= (position.x > 0))
                position.x = 0;
            if (needsRender |= (position.y > 0))
                position.y = 0;
            if (needsRender)
                forceRender();
        }
    }

    @Override
    public boolean isRenderable() {
        return super.isRenderable() && hostMask != null;
    }
}
