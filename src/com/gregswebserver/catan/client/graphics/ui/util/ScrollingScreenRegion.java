package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;

import java.awt.*;

/**
 * Created by greg on 1/21/16.
 * A screen region that supports smooth scrolling, and keeps within specified bounds.
 */
public abstract class ScrollingScreenRegion extends UIScreenRegion {

    private Point offset;
    private RenderMask hostMask;

    public ScrollingScreenRegion(int priority) {
        super(priority);
    }

    public void setHostView(RenderMask hostMask) {
        this.hostMask = hostMask;
        checkBounds();
    }

    // Rather than actually change the position, this changes the offset for scroll calculations.
    @Override
    public void setPosition(Point position) {
        this.offset = position;
        checkBounds();
    }

    @Override
    protected void resizeContents(RenderMask mask) {
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
            if (position.x < minX + offset.x) {
                position.x = minX + offset.x;
                needsRender = true;
            }
            if (position.y < minY + offset.y) {
                position.y = minY + offset.y;
                needsRender = true;
            }
            if (position.x > offset.x) {
                position.x = offset.x;
                needsRender = true;
            }
            if (position.y > offset.y) {
                position.y = offset.y;
                needsRender = true;
            }
            if (needsRender)
                forceRender();
        }
    }

    @Override
    public boolean isRenderable() {
        return super.isRenderable() && offset != null && hostMask != null;
    }
}
