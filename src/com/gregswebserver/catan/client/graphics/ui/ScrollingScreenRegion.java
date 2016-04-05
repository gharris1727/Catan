package com.gregswebserver.catan.client.graphics.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.screen.ScreenRegion;
import com.gregswebserver.catan.client.renderer.NotYetRenderableException;

import java.awt.*;

/**
 * Created by greg on 1/21/16.
 * A screen region that supports smooth scrolling, and keeps within specified bounds.
 */
public abstract class ScrollingScreenRegion extends ConfigurableScreenRegion {

    private final Point offset;
    private Insets insets;
    private ScreenRegion host;

    protected ScrollingScreenRegion(int priority, String configKey) {
        super(priority, configKey);
        this.offset = new Point();
    }

    public void setHost(ScreenRegion host) {
        this.host = host;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
        checkBounds();
    }

    // Rather than actually change the position, this changes the offset for scroll calculations.
    @Override
    public void setPosition(Point position) {
        this.offset.setLocation(position);
        checkBounds();
    }

    public void center() {
        if (isRenderable()) {
            Point position = getPosition();
            Dimension thisSize = getMask().getSize();
            Dimension hostMask = host.getMask().getSize();
            position.x = (hostMask.width - thisSize.width + insets.left - insets.right)/2 + offset.x;
            position.y = (hostMask.height - thisSize.height + insets.top - insets.bottom)/2 + offset.y;
            checkBounds();
        }
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
            Dimension thisSize = getMask().getSize();
            Dimension hostMask = host.getMask().getSize();
            int minX = hostMask.width - thisSize.width;
            int minY = hostMask.height - thisSize.height;
            if (position.x < minX + offset.x + insets.left)
                position.x = minX + offset.x + insets.left;
            if (position.y < minY + offset.y + insets.top)
                position.y = minY + offset.y + insets.top;
            if (position.x > offset.x - insets.right)
                position.x = offset.x - insets.right;
            if (position.y > offset.y - insets.bottom)
                position.y = offset.y - insets.bottom;
            host.forceRender();
        }
    }

    @Override
    public boolean isRenderable() {
        return super.isRenderable() && host != null && insets != null;
    }

    @Override
    public void assertRenderable() {
        super.assertRenderable();
        if (host == null)
            throw new NotYetRenderableException(this + " has no host");
        if (insets == null)
            throw new NotYetRenderableException(this + " has no insets");
    }
}
