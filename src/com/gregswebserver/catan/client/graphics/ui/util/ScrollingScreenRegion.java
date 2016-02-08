package com.gregswebserver.catan.client.graphics.ui.util;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.style.UIScreenRegion;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;

import java.awt.*;

/**
 * Created by greg on 1/21/16.
 * A screen region that supports smooth scrolling, and keeps within specified bounds.
 */
public abstract class ScrollingScreenRegion extends UIScreenRegion {

    private final Point offset;
    private RenderMask hostMask;
    private Insets insets;

    public ScrollingScreenRegion(int priority) {
        super(priority);
        this.offset = new Point();
    }

    public void setHostView(RenderMask hostMask, Insets insets) {
        this.hostMask = hostMask;
        this.insets = insets;
        checkBounds();
    }

    // Rather than actually change the position, this changes the offset for scroll calculations.
    @Override
    public void setPosition(Point position) {
        this.offset.setLocation(position);
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

    private void checkBounds() {
        if (isRenderable()){
            Point position = getPosition();
            Dimension size = getMask().getSize();
            Dimension host = hostMask.getSize();
            int minX = host.width - size.width;
            int minY = host.height - size.height;
            if (position.x < minX + offset.x + insets.left)
                position.x = minX + offset.x + insets.left;
            if (position.y < minY + offset.y + insets.top)
                position.y = minY + offset.y + insets.top;
            if (position.x > offset.x - insets.right)
                position.x = offset.x - insets.right;
            if (position.y > offset.y - insets.bottom)
                position.y = offset.y - insets.bottom;
            forceRender();
        }
    }

    @Override
    public boolean isRenderable() {
        return super.isRenderable() && offset != null && hostMask != null;
    }

    @Override
    public void assertRenderable() {
        super.assertRenderable();
        if (hostMask == null)
            throw new NotYetRenderableException(this + " has no host mask");
        if (insets == null)
            throw new NotYetRenderableException(this + " has no insets");
    }
}
