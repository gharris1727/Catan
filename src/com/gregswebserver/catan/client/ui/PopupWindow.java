package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.ui.ScrollingScreenRegion;
import com.gregswebserver.catan.client.input.UserEvent;
import com.gregswebserver.catan.client.input.UserEventType;

import java.awt.*;

/**
 * Created by greg on 4/3/16.
 * A popup window rendered on top of the ClientScreen
 */
public abstract class PopupWindow extends ScrollingScreenRegion {

    protected PopupWindow(String configKey) {
        super(2, configKey);
    }

    public UserEvent expire() {
        return new UserEvent(this, UserEventType.Expire_Popup, this);
    }

    @Override
    public UserEvent onMouseDrag(Point p) {
        scroll(p.x, p.y);
        return null;
    }
}
