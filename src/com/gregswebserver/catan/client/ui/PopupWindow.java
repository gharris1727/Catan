package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenRegion;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.*;

/**
 * Created by greg on 4/3/16.
 * A popup window rendered on top of the ClientScreen
 */
public abstract class PopupWindow extends ScrollingScreenRegion {

    private final ConfigurableScreenRegion source;

    protected PopupWindow(String name, String configKey, ConfigurableScreenRegion source) {
        super(name, 2, configKey);
        this.source = source;
    }

    public UserEvent display() {
        return new UserEvent(source, UserEventType.Display_Popup, this);
    }

    public UserEvent expire() {
        return new UserEvent(source, UserEventType.Expire_Popup, this);
    }

    @Override
    protected void updateBounds() {
        if (isRenderable()){
            Dimension thisSize = getMask().getSize();
            Dimension hostMask = getHostMask().getSize();
            minX = 0;
            minY = 0;
            maxX = hostMask.width - thisSize.width;
            maxY = hostMask.height - thisSize.height;
        }
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        scroll(p.x, p.y);
        return null;
    }
}
