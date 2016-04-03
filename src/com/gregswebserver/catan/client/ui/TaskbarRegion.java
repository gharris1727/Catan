package com.gregswebserver.catan.client.ui;

import com.gregswebserver.catan.client.graphics.masks.RenderMask;
import com.gregswebserver.catan.client.graphics.ui.ConfigurableScreenRegion;
import com.gregswebserver.catan.client.graphics.ui.TiledBackground;

/**
 * Created by greg on 4/3/16.
 * Taskbar at the top of the client screen.
 */
public class TaskbarRegion extends ConfigurableScreenRegion {

    private final TiledBackground background;

    public TaskbarRegion() {
        super(1, "taskbar");
        background = new TiledBackground(0, "background");
        add(background).setClickable(this);
    }

    @Override
    protected void resizeContents(RenderMask mask) {
        background.setMask(mask);
    }

    @Override
    public String toString() {
        return "Taskbar";
    }
}
