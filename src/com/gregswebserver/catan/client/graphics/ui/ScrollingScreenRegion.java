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

    private Insets insets;
    private ScreenRegion host;
    protected int minX;
    protected int minY;
    protected int maxX;
    protected int maxY;

    protected ScrollingScreenRegion(int priority, String configKey) {
        super(priority, configKey);
    }

    public void setHost(ScreenRegion host) {
        this.host = host;
        updateBounds();
    }

    protected RenderMask getHostMask() {
        return host.getMask();
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
        updateBounds();
    }

    // Rather than actually change the position, this changes the offset for scroll calculations.
    @Override
    public void setPosition(Point position) {
        getPosition().setLocation(position);
        checkBounds();
    }

    public void center() {
        if (isRenderable()) {
            host.center(this);
            checkBounds();
        }
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        updateBounds();
    }

    protected void updateBounds() {
        if (isRenderable()) {
            Dimension thisSize = getMask().getSize();
            Dimension hostMask = getHostMask().getSize();
            minX = hostMask.width - thisSize.width;
            minY = hostMask.height - thisSize.height;
            maxX = 0;
            maxY = 0;
            checkBounds();
        }
    }

    public void scroll(int x, int y) {
        getPosition().translate(x, y);
        checkBounds();
    }

    private void checkBounds() {
        Point position = getPosition();
        if (position.x < minX + insets.left)
            position.x = minX + insets.left;
        if (position.y < minY + insets.top)
            position.y = minY + insets.top;
        if (position.x > maxX - insets.right)
            position.x = maxX - insets.right;
        if (position.y > maxY - insets.bottom)
            position.y = maxY - insets.bottom;
        host.forceRender();
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
